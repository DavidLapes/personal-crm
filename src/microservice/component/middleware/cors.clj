(ns microservice.component.middleware.cors
  (:require [ring.middleware.cors :refer [wrap-cors]]))

(defn cors-middleware [handler]
  (wrap-cors
    handler
    :access-control-allow-origin [#"http://localhost:3000"
                                  #"http://localhost:3001"
                                  #"http://localhost:3002"
                                  #"https://admin.terkaberedavida.cz"
                                  #"https://www.terkaberedavida.cz"
                                  #"https://terkaberedavida.cz"
                                  #"https://en.terkaberedavida.cz"]
    :access-control-allow-methods [:get :put :post :delete :patch]))
