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
import scala.util.{Failure, Success, Try}

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
    //  Await.result(getExpandedSiblings(rel.siblings), 10 seconds)
        getExpandedSiblings(rel.siblings)
    )
  }

  def sleep(duration: Long) { Thread.sleep(duration) }

  def getExpandedSiblings(uprns: Seq[Long]): Seq[ExpandedSibling] = {
 //   val sibs = Seq[ExpandedSibling]()
    uprns.map(uprn => {
  //    val futUprn = getAddressFromUprn(uprn)
      new ExpandedSibling(uprn,Await.result(getAddressFromUprn(uprn), 2 seconds))
    })
  //  sleep(1000)
 //   println("sibs" + sibs)
 //   return Future(sibs)
  }

  def getAddressFromUprn(uprn: Long): Future[String] = {
    val numericUPRN = BigInt(uprn)
    apiClient.uprnQuery(
      AddressIndexUPRNRequest(
        uprn = numericUPRN,
        id = UUID.randomUUID,
        apiKey = ""
      )
    ).map { resp: AddressByUprnResponseContainer =>
      resp.response.address.map ({ add =>
        add.formattedAddress
      }).getOrElse(uprn + "not found")
   //   Try(resp.response.address.get.formattedAddress).getOrElse(uprn + "not found")
    }
  }
}
