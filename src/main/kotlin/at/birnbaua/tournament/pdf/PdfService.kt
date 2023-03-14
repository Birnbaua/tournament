package at.birnbaua.tournament.pdf

import at.birnbaua.tournament.data.document.Match
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.StringTemplateResolver
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path


/**
 * @author Birnbaua
 * @since 1.0
 */
@Service
class PdfService {

    private val log: Logger = LoggerFactory.getLogger(PdfService::class.java)
    private val resolver = StringTemplateResolver()
    private val engine = TemplateEngine()
    var templatePath = "data/match_report.html"

    init{
        log.info("Pdf-Service started...")
        resolver.templateMode = TemplateMode.TEXT
        engine.setTemplateResolver(resolver)
    }

    /**
     * Transforms a list of [Match] to Pdf-referee-sheets
     * @param matches All matches which should be transformed
     * @since 1.0
     */
    fun matchesToPdf(matches: List<Match>) : ByteArray {
        log.info("Transforming matches to pdf...")
        return toPdf(generateMatchesHTML(matches))
    }

    fun generateMatchesHTML(matches: List<Match>) : List<String> {
        log.info("Process matches to pdfs...")
        return matches.map { process(it) }
    }

    /**
     * Transforms a list of HTML-Pages to a single Pdf, with one HTML equals one page in the Pdf
     * @param content All pages of the desired Pdf
     * @since 1.0
     */
    fun toPdf(content: List<String>) : ByteArray {
        val renderer = ITextRenderer()
        val os = ByteArrayOutputStream()
        content.forEachIndexed { index, it ->
            renderer.setDocumentFromString(it)
            renderer.layout()
            if(index == 0) renderer.createPDF(os,false)
            if(index != 0) renderer.writeNextDocument()
        }
        renderer.finishPDF()
        return os.toByteArray()
    }

    fun process(match: Match) : String {
        val context = Context()
        context.setVariable("name","Spiele")
        context.setVariable("gameround",match.gameround)
        context.setVariable("match_no",match.no)
        context.setVariable("field",match.field)
        context.setVariable("team_a",match.teamA)
        context.setVariable("team_b",match.teamB)
        context.setVariable("referee",match.referee)
        return engine.process(loadOrCreateTemplate(),context)
    }

    fun loadOrCreateTemplate() : String {
        val file = File(templatePath)
        log.info("Loading template for matches...")
        if(!file.exists()) {
            log.error("Template for matches not existing. New empty template created.")
            Files.write(Path(templatePath),"".toByteArray(Charsets.UTF_8))
        }
        return file.readText(Charsets.UTF_8)
    }
}