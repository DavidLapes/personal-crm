(ns microservice.component.middleware.exception
  (:require [reitit.ring.middleware.exception :as exception]
            [ring.util.http-status :refer [internal-server-error unauthorized bad-request]]
            [taoensso.timbre :as timbre]
            [crm.lib.api.http-response :refer [response-message]])
  (:import (java.sql SQLException)
           (org.postgresql.util PSQLException)))

(def ^:private exception-codes
  {:expired-auth-token                     {:code    unauthorized
                                            :message "Přihlášení vypršelo, prosím, přihlašte se znovu"}
   :invalid-credentials                    {:code    unauthorized
                                            :message "Neplatné přihlašovací údaje"}
   :not-authenticated                      {:code    unauthorized
                                            :message "Nejsi přihlášen(a), prosím, přihlas se"}
   :rsvp-for-guest-already-answered        {:code    bad-request
                                            :message "Tvá účast může být potvrzena pouze jednou"}
   :modify-guest-failed                    {:code    internal-server-error
                                            :message "Úprava hosta se nezdařila"}
   :delete-guest-failed                    {:code    internal-server-error
                                            :message "Smazání hosta se nezdařilo"}
   :incorrect-type-when-assigning-escort   {:code    bad-request
                                            :message "Při přiřazování eskortu musí host být označen jako plus jedna, ne prioritní"}
   :missing-escort-for-plus-one-type-guest {:code    bad-request
                                            :message "Pro hosta typu plus jedna musí být přiřazen eskort"}
   :guest-being-escort-to-itself           {:code    bad-request
                                            :message "Host nemůže být eskortem sám sobě"}
   :escort-not-being-primary-guest         {:code    bad-request
                                            :message "Přiřazovaný eskort nemůže být plus jedna, pouze primární kontakt může mít plus jedna"}
   :delivery-type-missing-sent-status      {:code    bad-request
                                            :message "Při přidání typu doručení pozvánky musí být pozvánka označena jako doručená"}
   :sent-delivery-missing-type             {:code    bad-request
                                            :message "Při označení doručení pozvánky jako doručeno musíte určit i typ doručení"}
   :room-already-full                      {:code    bad-request
                                            :message "Pokoj je již v plné kapacitě."}})

(defn- get-exception-cause [exception]
  (let [class (type exception)]
    (condp isa? class
      PSQLException           (-> (.getServerErrorMessage exception) (.toString))
      (-> (ex-data exception) :cause))))

(defn- exception-handler [default-message exception request]
  (let [cause (get-exception-cause exception)]
    (let [response (if-let [known-exception (get-in exception-codes [cause])]
                     {:status (:code known-exception)
                      :body   (-> known-exception :message (response-message))}
                     {:status internal-server-error
                      :body   (str "Internal Server Error: " "{" default-message "} - " (or cause "unknown cause"))})]
      (println response)
      response)))

(def exception-middleware
  (exception/create-exception-middleware
    (merge
      exception/default-handlers
      {;; ex-data with :type ::error
       ::error             (partial exception-handler "error")

       ;; ex-data with ::exception or ::failure
       ::exception         (partial exception-handler "exception")

       ;; SQLException and all it's child classes
       SQLException        (partial exception-handler "sql-exception")

       ;; override the default handler
       ::exception/default (partial exception-handler "default")

       ;; print stack-traces for all exceptions
       ::exception/wrap    (fn [handler exception request]
                             (timbre/error exception)
                             (println "ERROR" (pr-str (:uri request)))
                             (handler exception request))})))
