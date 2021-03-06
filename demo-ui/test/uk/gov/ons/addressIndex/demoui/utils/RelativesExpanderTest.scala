package uk.gov.ons.addressIndex.demoui.utils

import org.scalatest.{FlatSpec, Matchers}
import play.api.i18n._
import play.api.mvc.ControllerComponents
import play.api.test.WithApplication
import uk.gov.ons.addressIndex.demoui.client.AddressIndexClientMock
import uk.gov.ons.addressIndex.model.db.index.{ExpandedRelative, ExpandedSibling, Relative}
import uk.gov.ons.addressIndex.model.server.response.AddressResponseRelative
import scala.concurrent.ExecutionContext.Implicits.global

class RelativesExpanderTest extends FlatSpec with Matchers {
  "RelativesExpander" should
    "add formattedAddresses to siblings in supplied relatives" in new WithApplication {
    val messagesApi = app.injector.instanceOf[MessagesApi]
    val langs = app.injector.instanceOf[Langs]
    val apiClient = app.injector.instanceOf[AddressIndexClientMock]
    val controllerComponents = app.injector.instanceOf[ControllerComponents]
    val relativesExpander = new RelativesExpander (apiClient)
    val rel1 = new AddressResponseRelative(1,Seq(1L),Seq())
    val rel2 = new AddressResponseRelative(2,Seq(2L,3L),Seq(1L))
    val rels = Seq(rel1,rel2)

    // Given
    val esib1 = new ExpandedSibling(1,"7, Gate Reach, Exeter, EX2 9GA")
    val esib2 = new ExpandedSibling(2,"7, Gate Reach, Exeter, EX2 9GA")
    val esib3 = new ExpandedSibling(3,"7, Gate Reach, Exeter, EX2 9GA")
    val ex1 = new ExpandedRelative(1,Seq(esib1))
    val ex2 = new ExpandedRelative(2,Seq(esib2,esib3))
    val expectedSeq = Seq(ex1,ex2)

    // When
    val result = relativesExpander.expandRelatives("antidisestablishmentarianism",rels)

    result shouldBe expectedSeq
  }

  }
