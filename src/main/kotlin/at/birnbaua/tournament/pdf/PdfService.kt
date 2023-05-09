package at.birnbaua.tournament.pdf

import at.birnbaua.tournament.data.document.Field
import at.birnbaua.tournament.data.document.Match
import at.birnbaua.tournament.data.document.Team
import at.birnbaua.tournament.data.document.sub.EmbeddedField
import at.birnbaua.tournament.data.document.sub.EmbeddedTeam
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
    private val lineSeperator = System.lineSeparator()
    var templatePath = "data/match_report.html"
    var maxLineLength = 15
    var maxLineCount = 3

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
    fun matchesToPdf(matches: List<Match>, name: String = "Spielberichtsbogen") : ByteArray {
        return toPdf(generateMatchesHTML(matches.sortedBy { it.no },name))
    }

    fun generateMatchesHTML(matches: List<Match>, name: String = "Spielberichtsbogen") : List<String> {
        log.info("Process matches to pdfs...")
        val template = loadOrCreateTemplate()
        return matches.map { process(it,template) }
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

    fun process(match: Match, template: String, name: String = "Spielberichtsbogen") : String {
        val context = Context()
        context.setVariable("title",name)
        context.setVariable("gameround",match.gameround)
        context.setVariable("match_no",match.no)
        context.setVariable("field",if(match.field != null) match.field!!.process() else EmbeddedField(-1,""))
        context.setVariable("team_a",if(match.teamA != null) match.teamA!!.process() else EmbeddedTeam(-1,""))
        context.setVariable("team_b",if(match.teamB != null) match.teamB!!.process() else EmbeddedTeam(-1,""))
        context.setVariable("referee",if(match.referee != null) match.referee!!.process() else EmbeddedTeam(-1,""))
        return engine.process(template,context)
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

    private fun EmbeddedField.process() : EmbeddedField {
        this.name = this.name?.chunked(maxLineLength)?.joinToString(lineSeperator, limit = maxLineCount)
        return this
    }

    private fun EmbeddedTeam.process() : EmbeddedTeam {
        this.name = this.name.chunked(maxLineLength).joinToString(lineSeperator, limit = maxLineCount)
        return this
    }
}