package uk.gov.ons.addressIndex.demoui.utils

import java.util.UUID

import uk.gov.ons.addressIndex.model.db.index.{ExpandedRelative, ExpandedSibling, Relative}
import uk.gov.ons.addressIndex.model.server.response.{AddressByUprnResponseContainer, AddressResponseRelative}
import javax.inject.{Inject, Singleton}
import uk.gov.ons.addressIndex.demoui.client.AddressIndexClientInstance
import uk.gov.ons.addressIndex.model.AddressIndexUPRNRequest

import scala.language.postfixOps
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.Try

@Singleton
class RelativesExpander @Inject ()(
  apiClient: AddressIndexClientInstance
)(implicit ec: ExecutionContext) {

  def expandRelatives(relatives: Seq[AddressResponseRelative]): Seq[ExpandedRelative] = {
    relatives.map{rel => expandRelative(rel)}
  }

  def expandRelative (rel: AddressResponseRelative):  ExpandedRelative = {
    ExpandedRelative (
      rel.level,
      getExpandedSiblings(rel.siblings)
    )
  }

  def getExpandedSiblings(uprns: Seq[Long]): Seq[ExpandedSibling] = {
    uprns.map(uprn => new ExpandedSibling(uprn,Await.result(getAddressFromUprn(uprn),1 seconds)))
  }

  def getAddressFromUprn(uprn: Long): Future[String] = {
    val numericUPRN = BigInt(uprn)
    apiClient.uprnQuery(
      AddressIndexUPRNRequest(
        uprn = numericUPRN,
        id = UUID.randomUUID,
        apiKey = ""
      )
    ) map { resp: AddressByUprnResponseContainer =>
      Try(resp.response.address.get.formattedAddress).getOrElse("not found")
    } map {relstring => relstring.mkString}
  }
}
