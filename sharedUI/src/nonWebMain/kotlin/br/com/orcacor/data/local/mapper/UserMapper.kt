package br.com.orcacor.data.local.mapper

import br.com.orcacor.data.local.entity.UserEntity
import br.com.orcacor.domain.entity.User

fun UserEntity.toDomain(): User = User(id = id, name = name, email = email, phone = phone)
fun User.toEntity(): UserEntity = UserEntity(id = id, name = name, email = email, phone = phone)
