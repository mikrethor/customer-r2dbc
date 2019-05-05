package com.digitowl.r2dbc.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("customer")
class Customer(@Id
               var id: Long? = null,
               var firstname: String,
               var lastname: String
)