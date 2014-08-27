# ![ironsites](//raw.githubusercontent.com/steeleforge/ironsites/master/src/site/resources/ironsites_32x32.png "ironsites") ironsites
##### AEM better

## Motivation
ironsites is not some grand, turn-key solution for orgs running on CQ, but rather a set of features that provides direction to CQ developers re-solving the same problems. The goal of ironsites is to provide meta 'framework'y patterns that CQ software architects infrequently have time to build, but have to roughly describe to developer/leads in design guidance, or worse, in code reviews. The project objectives are general because the scope should grow to capture new patterns as the need arises. To summarize the hopes of ironsites:

+ Desire to see out-of-box APIs used appropriately
+ Reduce boilerplate when possible
+ Establishing patterns and conveniences
+ Avoid building these patterns/conveniences again-and-again

## Getting Started
To get started, ensure you have a running instance of CQ5.6 and can run mvn goals. Locate the parent pom.xml file for ironsites and edit the profile properties for local-author and/or local-publish. The default profile is local-author running on localhost:4502 with admin/admin credentials. Adjust this to suit your local environments.

To view a maven generated site with javadocs run the following goal then open a browser to the host/port mentioned in the output. This will help you navigate the features much better than this readme will.
> mvn site:run

To deploy to a local author instance can be performed by running the 'install' maven goal. 
> mvn clean install -P local-author

To deploy to a local publish instance, qualify the environment.
> mvn clean install -P local-publish

For AEM 5.6.x specifically, a developmer must also include the profile flag for **aem5**. This is to address a dependency in the simple cache sub-module. AEM 6 includes com.google.commons found in guava v15 where previous versions do not. The pom is setup to export this dependency for previous AEM versions. The below is an example command:
> mvn clean install -P local-author,aem5

## Dependencies
+ Adobe Experience Management (CQ) 5.6, 6
+ JDK 1.5/1.6
+ maven3
+ Patience required for exploring alpha-level software

## Features
+ [Internationalization (i18n) Helpers](https://github.com/steeleforge/ironsites/wiki/Internationalization-Helpers)
+ [Cross-Site Scripting (XSS) Helpers](https://github.com/steeleforge/ironsites/wiki/Cross-Site-Scripting-Helpers)
+ [WCMMode Helpers](https://github.com/steeleforge/ironsites/wiki/WCMMode-Helpers)
+ [Simple Cache](https://github.com/steeleforge/ironsites/wiki/Simple-Cache)
+ [Components](https://github.com/steeleforge/ironsites/wiki/Components)
+ [Misc.](https://github.com/steeleforge/ironsites/wiki/Miscellaneous)

### Internationalization (i18n) Helpers [[wiki]](https://github.com/steeleforge/ironsites/wiki/Internationalization-Helpers)
+ Sane patterns for content-managed i18n copy in CQ components with reasonable Sling supported fallbacks as well as low-effort access to the CQ out-of-box [i18n translation helper]( http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/i18n/I18n.html).
+ Conveniences for supporting MessageFormat interpolation in i18n-ready copy.
+ Optional selector servlet to provide JSON serialzation of sling messages associated to a component/resourceType (e.g. /content/path/to/component.i18n.json) for usage with Javascript rich-client libraries.

### XSS Helpers [[wiki]](https://github.com/steeleforge/ironsites/wiki/Cross-Site-Scripting-Helpers)
+ taglib functions for [XSSAPI](http://dev.day.com/docs/en/cq/current/javadoc/com/adobe/granite/xss/XSSAPI.html) such as encoding for XML/Javasscript/HTML.  
+ Filtering markup based on [XSSFilter](http://dev.day.com/docs/en/cq/current/javadoc/com/adobe/granite/xss/XSSFilter.html) and [AntiSamy policy files](https://www.owasp.org/index.php/Category:OWASP_AntiSamy_Project#Stage_3_-_Tailoring_the_policy_file) which can be managed in the JCR/CRX.

### WCMMode Helpers [[wiki]](https://github.com/steeleforge/ironsites/wiki/WCMMode-Helpers)
+ Elimination of scriptlet boilerplate for driving component display behavior based on WCMMode.

### Simple Cache [[wiki]](https://github.com/steeleforge/ironsites/wiki/Simple-Cache)
+ Basic application-level caching somewhere between a heavy Enterprise library and a basic ConcurrentHashMap.

### Components [[wiki]](https://github.com/steeleforge/ironsites/wiki/Components)
+ ironsites Sitemap is a drop-in replacement for the CQ foundation Sitemap
+ ironsites Text component provides an example of HTML filtering utilizing XSS Helpers
+ ironsites Cache Monitor allows basic reporting on Simple Cache usage/access
+ ironsites Kitchen Sink is a demo component showcasing as much of the above features as reasonable for a developer that would like to build upon what is available

### Misc. [[wiki]](https://github.com/steeleforge/ironsites/wiki/Miscellaneous)
+ Composable, flexible, and configurable alternatives to [PageFilter](http://dev.day.com/docs/en/cq/current/javadoc/com/day/cq/wcm/api/PageFilter.html) which is commonly used to create navigation and sitemap components.

## Great projects that improve CQ development
+ [recap - rsync for Adobe Granite](https://github.com/adamcin/net.adamcin.recap)
+ [vltpack - vault package maven plugin](https://github.com/adamcin/vltpack-maven-plugin)
+ [net.adamcin.commons.jcr - batch JCR operations](https://github.com/adamcin/net.adamcin.commons.jcr)
+ [pyslinger - support utility for content migration](https://github.com/sevennineteen/pyslinger)
+ [AEM commons - additional utilities](https://github.com/Adobe-Consulting-Services/acs-aem-commons)

## Author
[site](http://www.steeleforge.com) | [twitter](http://www.twitter.com/davidsteele) |  [gittip](https://www.gittip.com/steeleforge/)

## Licence
[unlicense](http://unlicense.org)
