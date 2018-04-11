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

  def expandRelatives(apiKey: String, relatives: Seq[AddressResponseRelative]): Seq[ExpandedRelative] = {
    relatives.map{rel => expandRelative(apiKey,rel)}
  }

  def expandRelative (apiKey: String, rel: AddressResponseRelative):  ExpandedRelative = {
    ExpandedRelative (
      rel.level,
    //  Await.result(getExpandedSiblings(rel.siblings), 10 seconds)
        getExpandedSiblings(apiKey, rel.siblings)
    )
  }

  def sleep(duration: Long) { Thread.sleep(duration) }

  def getExpandedSiblings(apiKey: String, uprns: Seq[Long]): Seq[ExpandedSibling] = {
 //   val sibs = Seq[ExpandedSibling]()
    uprns.map(uprn => {
  //    val futUprn = getAddressFromUprn(uprn)
      new ExpandedSibling(uprn,Await.result(getAddressFromUprn(apiKey,uprn), 2 seconds))
    })
  //  sleep(1000)
 //   println("sibs" + sibs)
 //   return Future(sibs)
  }

  def getAddressFromUprn(apiKey: String, uprn: Long): Future[String] = {
    val numericUPRN = BigInt(uprn)
    apiClient.uprnQuery(
      AddressIndexUPRNRequest(
        uprn = numericUPRN,
        id = UUID.randomUUID,
        apiKey = apiKey
      )
    ).map { resp: AddressByUprnResponseContainer =>
      println(resp)
      resp.response.address.map ({ add =>
        add.formattedAddress
      }).getOrElse(uprn + "not found")
   //   Try(resp.response.address.get.formattedAddress).getOrElse(uprn + "not found")
    }
  }
}
