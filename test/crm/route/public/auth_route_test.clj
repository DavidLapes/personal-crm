(ns crm.route.public.auth-route-test
  (:require [clojure.test :refer :all]
            [crm.lib.http.client :as client]
            [crm.route.route-test :refer [get-match get-endpoint build-public-path build-full-public-path]])
  (:import (reitit.core Match)))

(deftest auth-endpoint-test
  (let [expected-path   (build-public-path "/auth/sign-in")
        full-path       (build-full-public-path "/auth/sign-in")
        path-match      (get-match expected-path)]
    (testing (str "Routes contain " expected-path " endpoint(s)")
      (is (instance? Match path-match)))
    (testing (str "POST " expected-path " endpoint present")
      (let [endpoint              (get-endpoint path-match :post)
            {:keys [method path]} endpoint]
        (is (= path expected-path))
        (is (= method :post))))
    (testing (str "Wrong credentials " expected-path " returns 401")
      (let [{status :status} (client/request {:method :post
                                              :uri    full-path
                                              :body   {:email    "NON_EXISTING_EMAIL"
                                                       :password "NON_EXISTING_PASSWORD"}})]
        (is (= status 401))))
    (testing (str "Correct credentials " expected-path " returns 200")
      (let [{status :status} (client/request {:method :post
                                              :uri    full-path
                                              :body   {:email    "admin@admin.com"
                                                       :password "admin"}})]
        (is (= status 200))))))
