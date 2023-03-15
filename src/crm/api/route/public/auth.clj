(ns crm.api.route.public.auth
  (:require [crm.api.controller.auth :as controller]
            [crm.api.schema.auth :refer [SignIn AuthenticationToken]]
            [crm.lib.api.router :refer [wrap-with-context]]))

(def routes
  ["/auth"
   {:swagger {:tags ["Authentication"]}}

   ["/sign-in"
    {:post {:summary "Create new account"
            :responses {200 {:body AuthenticationToken}}
            :parameters {:body SignIn}
            :handler (wrap-with-context
                       (fn [request]
                         (controller/sign-in request)))}}]])
