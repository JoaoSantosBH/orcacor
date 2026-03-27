package br.com.orcacor.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class WindowKind(val id: Int, val name: String, val area: Float)

@Serializable
data class DoorKind(val id: Int, val name: String, val area: Float)

@Serializable
data class MirrorKind(val id: Int, val name: String, val area: Float)

@Serializable
data class ClosetKind(val id: Int, val name: String, val area: Float)
