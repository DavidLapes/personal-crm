(ns crm.api.controller.secure-ping
  (:require [crm.lib.api.http-response :refer [response-message]]
            [ring.util.http-response :refer [ok]]))

(defn secure-ping []
  (ok (response-message "The API was securely pinged!")))
