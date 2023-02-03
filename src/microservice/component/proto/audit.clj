(ns microservice.component.proto.audit)

(defprotocol AuditLogger
  (-persist [this data]))
