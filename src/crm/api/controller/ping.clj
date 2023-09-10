(ns crm.api.controller.ping
  (:require [crm.lib.api.http-response :refer [response-message]]
            [ring.util.http-response :refer [ok]]))

(defn ping []
  (ok (response-message "The API was pinged!")))
