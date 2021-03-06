package betamax.examples

import co.freeside.betamax.Betamax
import co.freeside.betamax.Recorder
import co.freeside.betamax.httpclient.BetamaxRoutePlanner
import com.gargoylesoftware.htmlunit.ProxyConfig
import geb.spock.GebSpec
import grails.util.BuildSettingsHolder
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.junit.Rule
import org.openqa.selenium.htmlunit.HtmlUnitDriver

class TwitterPageSpec extends GebSpec {

	File baseDir = BuildSettingsHolder.settings?.baseDir ?: new File('examples/grails-betamax')
	@Rule Recorder recorder = new Recorder(tapeRoot: new File(baseDir, 'test/resources/tapes'), ignoreLocalhost: true)

	void setupSpec() {
		def restClient = ApplicationHolder.application.mainContext.restClient
		BetamaxRoutePlanner.configure(restClient.client)
	}

	void setup() {
		browser.baseUrl = 'http://localhost:8080/grails-betamax/'
		HtmlUnitDriver driver = browser.driver
		def proxyConfig = new ProxyConfig('localhost', 5555)
		proxyConfig.addHostsToProxyBypass('localhost')
		driver.webClient.proxyConfig = proxyConfig
		//driver.setJavascriptEnabled(true) //not work. see http://groups.google.com/group/webdriver/browse_thread/thread/6bbb18dca79c4e92?pli=1
	}

	@Betamax(tape = 'twitter success')
	void 'displays list of tweets based on query'() {
		given:
		go 'twitter'

		expect:
		title == 'Twitter Search Results'

		and:
		$('#tweets li').size() == 10
		$('#tweets li p')*.text().every { it =~ /(?i)#gr8conf/ }
		$('#tweets li').eq(0).find('p').text() == 'Slides for my #gr8conf talk: \u2018Spock Soup to Nuts\u2019. Thanks to everyone who showed up! http://t.co/CNA9ertp'
	}

	@Betamax(tape = 'twitter success')
	void 'can follow a link to a twitter user'() {
		given:
		go 'twitter'

		when:
		$('#tweets li').eq(0).find('small a').click()

		then:
		$('.user-info .username').text() == '@zanthrash'
	}

}
