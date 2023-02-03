(ns crm.lib.db.pagination
  (:require [clojure.walk :refer [keywordize-keys]]
            [honeysql.helpers :as honey]))

(defn- extract-limit
  "Returns limit filter from filters map."
  [{:keys [limit]}]
  (when limit
    (cond
      (integer? limit) limit
      :else (Integer/parseInt limit))))

(defn- extract-offset
  "Returns offset filter from filters map."
  [{:keys [page_number limit] :as filters}]
  (when (and page_number limit)
    (let [limit (extract-limit filters)
          page-number (cond
                        (integer? page_number) page_number
                        :else (Integer/parseInt page_number))]
      (when (and limit page-number)
        (when (< limit 0)
          (throw (ex-info "Limit can not be less than 0" {:type  :validation
                                                          :cause :limit-less-than-zero})))
        (when (< page-number 0)
          (throw (ex-info "Page number can not be less than 0" {:type  :validation
                                                                :cause :page-number-less-than-zero})))
        (* limit (- page-number 1))))))

(defn apply-paging-query
  "Applies pagination filters to HoneySQL query."
  [query filters]
  (let [filters (keywordize-keys filters)
        limit (extract-limit filters)
        offset (extract-offset filters)]
    (cond-> query
            (some? limit) (honey/limit (extract-limit filters))
            (and (some? offset)
                 (some? limit)) (honey/offset offset))))
