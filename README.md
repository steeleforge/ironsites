![ironsites](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA3XAAAN1wFCKJt4AAAAB3RJTUUH3QYBFB8WdymH9wAABKNJREFUWMOtl2+IVVUQwH/nencfu/7ZFTd3u1u0+sQ002XveT2j2lrFKDBbjP5YEEGEhPbnQxQkFf35FgpSfYmIEtIiSjCiEpc+iFlvffPUZS0hYa3Wd3eD2KUN7G3unj5073o93avvWffL3DMzZ2bOzJyZOYoaPq11O3An0AksAuYCBvgdGFJKFY0xB0RktFqZqkrFjwFbgK4q5X4HvCkie9atW0dfX18qo5OiEADf9zdqrX8F3qlBOcCNwG6t9c9jY2M9cZmX9IDWGhFBa70beIj/53tDRJ72fZ9SqZRugNaayclJt76+fhC4NkXYFFAwxgw6jjMKKGNMO7ACyF/EiEMi0h0d8F8G5HI5isUiWusfgGUJAk4Dr4jI+2kaenp61MTExBPAC8DCBJaDInJbpAtgVkQpl8torT8EehI2viQi9wRBcCyXy1EulxMNaGlpQUT6gyDY7nneXOAmi+Uaz/MaRaQvyQN3G2P2WRsqwFoROVzjdY3yaAOwLyHX8tPT00eOHj36D6G3t1cNDw+PAldYjDfXqjzBmI3AXgv9o4gsnQnBnDlzHgfuT3D77v+a/kEQnPQ8byFwQwy9wPO8gSAITkZ1YIu1b0hEXout116G7laAzs5ORGQrMG7RnwJwfN9vB1bap7fWO4DNNShfA3wEcPz48Qj3un1ptNbzHKXUeovwl4h84Pt+HPc88DbQXGV5/wLYFk9K13V3JPB2O8D1FvIIYFes/UAAfByPI/At8DXQEMPvBMohDQARoVAoTALfW7pWO0A2jjHGnEiw1ADHgNtjuPqw5q8B6mL4rcCXKd4ZstaeAzRd4D+lfkrZ/HkIbwnhbzHanyHsCG9WITE2StkVrNkBpi0PpLXoIIRXhXDa6g8Ay0P4S5IAY4zdfY1jXw+l1OIUA/4IYVSsZsVo0X97CNMGknZrPe4kxGX5JTxwR9hoOmK0K4E2wA/zZSRFxiJrfcZVSh0zxlwwTDQ1NalsNmusmxAl2vqEE562wmHs3nDu3LnGhBZ/yEnK2Gw2u9keHICrqyxCs+yTigiu6z6XwFtwisXiCFC08uBlgCVLlsTR82uohDPhyefz+L6vgGctnv0iMhFl5VsWsU1rvf3UqVNx3K2XY0B/fz9KqV1Ao8Wzc2YoFZFdwLDF8IzW+q7YemkNBqyIxf9R4GGLPigiX80Y0NHRAfBIgqDPtNa94f+8GgxYFlP+bkI9eDCXy52/v+Pj4wRBMOR5Xos1WCpgk+d5C4Ig6LZq/sW+6Xw+f50x5tUE2rZSqbQ3GutUwhj1TcIsx9TUFCMjI4yOjmJd2/OPDMehra2N1tZWHCfxyfGpiNy7atUqBgYGksfy0IiDQHdCLUcpxdmzZ6lUKlQqFZRSZDIZ6uvraWhowBiTZuAnInJffCK2yylBEERGvOd53vyw29nxw3VdMpkMs2fPprGxkUwmg+u6qZ4xxrxYKpWe1FqjlLpgqk5sPLE3wmpgD7D4MkfCQWCTiJxIehWlvg0jF9XV1RVEJAs8ABysQfEBY8wGEVkZzRdJyqt+HUdfV1dXs+M43cDqsAHND+v+GHAGOAwcFpGJamX+DZeerfgbb0IAAAAAAElFTkSuQmCC "ironsites") ironsites <sub style="font-size: 0.5em">AEM better</sub>
=========

Motivation
-----------
ironsites is not some grand, turn-key solution for orgs running on CQ, but rather a set of features that provides direction to CQ developers re-solving the same problems. The goal of ironsites is to provide meta 'framework'y patterns that CQ software architects infrequently have time to build, but have to roughly describe to developer/leads in design guidance, or worse, in code reviews. The project objectives are general because the scope should grow to capture new patterns as the need arises. To summarize the hopes of ironsites:

+ Desire to see out-of-box APIs used appropriately
+ Reduce boilerplate when possible
+ Establishing patterns and conveniences
+ Avoid building these patterns/conveniences again-and-again

Getting Started
-----------
To get started, ensure you have a running instance of CQ5.6 and can run mvn goals. Locate the parent pom.xml file for ironsites and edit the profile properties for local-author and/or local-publish. The default profile is local-author running on localhost:4502 with admin/admin credentials. Adjust this to suit your local environments.

To view a maven generated site with javadocs run the following goal then open a browser to the host/port mentioned in the output. This will help you navigate the features much better than this readme will.
> mvn site:run

To deploy to a local author instance can be performed by running the 'install' maven goal. 
> mvn clean install

To deploy to a local publish instance, qualify the environment.
> mvn clean install -Denv=local-publish

Dependencies
-----------
+ Adobe Experience Management (CQ) 5.6
+ JDK 1.5/1.6
+ maven3
+ Patience required for exploring alpha-level software

Features
-----------

### i18n Helpers
+ Sane patterns for content-managed i18n copy with reasonable Sling supported fallbacks as well as low-effort access to the CQ out-of-box [i18n translation helper]( http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/i18n/I18n.html).
+ Some copy must support String interpolation (e.g. "Hello {name}, how is the weather in {city}, but this becomes difficult when sentence order is not guaranteed. Just look at all that messy contractor code performing concatenation and asking authors to fill dialog textfield for the sentence fragment " of " -- gross. java.text.MessageFormat makes things slightly better if your authors are willing to put up with: "Hello {0}, how is the weather in {1}". Otherwise, String replacement works in a pinch. This is what the CQ I18n class helps with, and ironsites makes it accessible with a single tag.

<!-- language-all: java -->
	<%=i18n.get(StringUtils.replaceEach(
			properties.get("greeting",""),
			new String[]{"{name}", "{city}"}, new String[]{"{0}", "{1}"}),
				null,
				"David", "Detroit, MI") %>

+ Hard-coding copy in your JSPs is an anti-pattern; even if you need a placeholder default to maintain formatting. CQ5.5+ provides a translation UI that allows an experienced user to avoid that painful deploy due to a mispelling that was forgotten about in a dialog if you prepare in advance for it and make use of [sling internationlization support](http://sling.apache.org/documentation/bundles/internationalization-support-i18n.html). ironsites prescribes a development pattern and taglib to give developers a pattern to repeat, because the following common case is still practically hard-coding.

<!-- language-all: java -->
	<%=properties.get("greeting", "Hello World")%>

+ The preference let sling do it's job with sling:MessageEntry nodes.

<!-- language-all: xml -->
	<?xml version="1.0" encoding="UTF-8"?>
	<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
	    jcr:primaryType="sling:Folder"
	    jcr:language="en"
	    jcr:title="English"
	    jcr:mixinTypes="[mix:language]"
	    sling:basename="[fooapp/components/barcomponent]">
	    <greeting jcr:primaryType="sling:MessageEntry"
    		sling:key="greeting"
    		sling:message="Hello World"/>
	</jcr:root>

<!-- language-all: java -->
	<%=messages.get("greeting", "[greeting]")%>

+ An ironsites tag makes available the above "messages" variable which does the work of finding a local property on a component instance resource/node and presenting that, or otherwise falling back to a component-wide default fallback based on sling:basename matching your component sling:resourceType.
+ The above may appear to be more work but it is worthwhile. Component dialogs will bloat as all component copy becomes some configurable properties; some copy doesn't need to burden the authoring community. Additionally, some developers will forget to make some copy authorable and inadvertantly bake-in defaults with no means to author them. In production, this could mean some untranslatable text could necessitates a patch deployment. As an alternative to this scenario, if developers are mandated to create sling:MessageEntry nodes for all copy and perhaps fallback to last-resort signals (such as `[greeting]` in the above scriptlet) to indicate absense of a sling:MessageEntry, these nodes can be reliably updated on a running instance with the [CQ5 Translator UI (note: change host/port)](http://localhost:4502/libs/cq/i18n/translator.html) and corrected in the next regular deployment. 

### XSS Helpers
+ taglib functions for [XSSAPI](http://dev.day.com/docs/en/cq/current/javadoc/com/adobe/granite/xss/XSSAPI.html) such as encoding for XML/Javasscript/HTML.  
+ Filtering markup based on [XSSFilter](http://dev.day.com/docs/en/cq/current/javadoc/com/adobe/granite/xss/XSSFilter.html) and [AntiSamy policy files](https://www.owasp.org/index.php/Category:OWASP_AntiSamy_Project#Stage_3_-_Tailoring_the_policy_file) which can be managed in the JCR/CRX.
+ Well-travelled CQ developers will have frequently been to arbitrarily parse/filter out HTML/CSS/etc for various valid and invalid reasons. Well you could still do that, or you could use an AntiSamy policy file with granular rules. That's up to you, but ironsites makes the latter course of action easier on the developer.

<!-- language-all: java -->
	<isx:filterHTML policy="ripout-javascript-from-authors.xml">
		<cq:text property="text"/>
	</isx:filterHTML>

### WCMMode Helpers
+ Avoidance of scriptlet boilerplate for driving component display behavior based on WCMMode.

<!-- language-all: java -->
    <% if (!WCMMode.EDIT.equals(WCMMode.fromRequest(sling.getRequest()))) { %> ... <% } %>

feels batman

<!-- language-all: java -->
    <c:if test="${'EDIT' ne wcmmode}">...<c:if>
    
feels betterman
    
<!-- language-all: java -->
    <c:if test="${not mode.edit}">...<c:if>

feels goodman

### Simple Cache
+ Basic application caching powered by [Google Guava](https://code.google.com/p/guava-libraries/wiki/CachesExplained) which can be managed by OSGi configurations or created ad-hoc.
+ If you have the memory the spare, and want to skip re-computing/instantiating frequently accessed objects, why not use an application cache? You probably have a good reason not to, but if you need a slim & simple caching mechanism Google Guava is a reasonable route to go.
+ SimpleCacheService is an OSGi managed service that provides access too configurable instances of [Cache](http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/cache/Cache.html).

### Extended Page Filter
+ Composable, flexible, and configurable alternatives to [PageFilter](http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/PageFilter.html) which is commonly used to create navigation and sitemap components.
+ 'hideInHav' & Page#isValid are possibly not the only criteria you want to use when generating navigation, sitemaps, archive lists, etc. ironsites has implementations of `Filter<Page>` which have been broken down into a few more _primitive_ filters that can be composed together.

### Components & Misc.
+ Small demo components which highlight some of the above patterns and how a developer might use them.
    + ironsites Sitemap extends the foundation component to support forced inclusions, forced exclusions, and generating [sitemap.xml](http://sitemaps.org).
    + ironsites Text extends the foundation component to support AntiSamy HTML filtering.
    + ironsites Cache Monitor is a simple way to find out what your Simple Cache instances have been up to (providing that recording stats is enabled).
+ The above features rely on a few utility singletons with static methods that developers may benefit from re-using. For example, taglib developers will find a number of useful methods in WCMUtils for acquiring SlingScriptHelper or SlingHttpServletRequest from PageContext. Another useful method is WCMUtil#getFastHash which uses Guava to build a repeatable hash given a String. Hashing information like Resource#getPath() could be used for generating DOM class/IDs to hang CSS and Javascript while avoiding IDs containing paths like "cq-image-jsp-/content/geometrixx/en/home/_jcr_content/par/image_0"; doing this wouldprovide a better uniqueness guarantee than a bad habit of using System#currentTimeMillis() or AtomicInteger.

Great projects that improve CQ development
-----------
+ [recap - rsync for Adobe Granite](https://github.com/adamcin/net.adamcin.recap)
+ [vltpack - vault package maven plugin](https://github.com/adamcin/vltpack-maven-plugin)
+ [net.adamcin.commons.jcr - batch JCR operations](https://github.com/adamcin/net.adamcin.commons.jcr)
+ [pyslinger - support utility for content migration](https://github.com/sevennineteen/pyslinger)
+ [activecq - samples & patterns](https://github.com/activecq)

Author
-----------
+ [site](http://www.steeleforge.com)
+ [twitter](http://www.twitter.com/davidsteele)

Licence
-----------
[unlicense](http://unlicense.org)