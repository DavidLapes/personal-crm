(ns microservice.component.proto.impl.encoding
  (:require [cheshire.generate :refer [add-encoder]])
  (:import (java.time LocalDateTime)
           (java.time.format DateTimeFormatter)))

(add-encoder LocalDateTime
             (fn [date jsonGenerator]
               (let [formatter DateTimeFormatter/ISO_LOCAL_DATE_TIME
                     encoded-value (.format date formatter)]
                 (.writeString jsonGenerator encoded-value))))
