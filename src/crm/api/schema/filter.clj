(ns crm.api.schema.filter)

(def general-sql-filters #{:limit :order_column :order_direction :order_limit :page_number})

(def user-filters
  #{:id :email :name :person_id
    :is_active :is_deleted :time_created})
