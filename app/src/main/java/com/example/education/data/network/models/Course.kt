package com.example.education.data.network.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class Course(
    val id: Int,
    val title: String,
    val summary: String,
    @SerialName("display_price") val price: String,
    val rating: Double = 4.9,
    @Serializable(ApiLocalDateTimeSerializer::class)
    @SerialName("create_date") val date: LocalDateTime,
    @SerialName("cover") val cover: String,
    var isSaved: Boolean = false
) {
    val edited_summary: String
        get() = summary.replace("\n", "").trim()
//    val edited_date: String
//        get() = create_date.substring(0, 10)
}

@Serializable
data class CourseResponse(
    @SerialName("courses")
    val courses: List<Course>,
)

object ApiLocalDateTimeSerializer :
    KSerializer<LocalDateTime> by LocalDateTimeSerializer(formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME)
//    KSerializer<LocalDateTime> by LocalDateTimeSerializer(formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD'T'hh:mm:ss'Z'"))



private class LocalDateTimeSerializer(
    val formatter: DateTimeFormatter
) : KSerializer<LocalDateTime> {
    override val descriptor =
        PrimitiveSerialDescriptor(LocalDateTime::javaClass.name, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) =
        encoder.encodeString(value.format(formatter))

    override fun deserialize(decoder: Decoder): LocalDateTime =
        LocalDateTime.parse(decoder.decodeString(), formatter)
}
