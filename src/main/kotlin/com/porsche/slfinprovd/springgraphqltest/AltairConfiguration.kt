package com.porsche.slfinprovd.springgraphqltest

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.view.RedirectView

private const val ALTAIR_BASE_PATH_PATTERN = "classpath:META-INF/resources/webjars/altair-static/*/build/dist/"

private fun ResourceLoader.getAltairIndexHtml() = ResourcePatternUtils.getResourcePatternResolver(this).getResources(ALTAIR_BASE_PATH_PATTERN + "index.html").single()

private fun ResourceLoader.getAltairVersion() = getAltairIndexHtml()
    .url
    .toString()
    .split("/")
    .let { it[it.indexOf("altair-static") + 1] }

@Configuration
@EnableWebMvc
class AltairConfiguration(
    private val resourceLoader: ResourceLoader,
) : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/altair/**").addResourceLocations(ALTAIR_BASE_PATH_PATTERN.replace("*", resourceLoader.getAltairVersion()))
    }
}

@Controller
class AltairController(
    @Value("\${spring.graphql.path:/graphql}") private val graphQLPath: String,
    private val resourceLoader: ResourceLoader,
) {
    private val html by lazy {
        resourceLoader.getAltairIndexHtml().inputStream.bufferedReader().use { it.readText() }.replace("<base href=\"/\" />", "<base href=\"/altair/\"").replace(
            "<body>",
            """
            <body><script>
            window.__ALTAIR_ENDPOINT_URL__=`${'$'}{window.location.origin}$graphQLPath`
            document.addEventListener('DOMContentLoaded',()=>{AltairGraphQL.init()})
            </script>
            """.trimIndent(),
        )
    }

    @GetMapping(path = ["/altair/", "/altair/index.html"], produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun index(): String = html

    @RequestMapping(value = ["/altair", "/playground", "/playground/**", "/graphiql", "/graphiql/**"])
    fun redirect(): RedirectView = RedirectView("/altair/").apply {
        setStatusCode(HttpStatus.MOVED_PERMANENTLY)
    }
}
