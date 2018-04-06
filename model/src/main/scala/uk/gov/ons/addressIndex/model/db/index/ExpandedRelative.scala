package uk.gov.ons.addressIndex.model.db.index

/**
  * Relative DTO
  * Relatives response contains a sequence of Relative objects, one per level
  */
case class ExpandedRelative(
  level: Int,
  siblings: Seq[Long],
  formattedAddresses: Seq[String]
)

