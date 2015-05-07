package lj.taos.tags

import org.springframework.web.servlet.support.RequestContextUtils

class PageTagLib {
    static namespace = "taos"

    def paginate = {attrs ->
        def total = attrs.total.toInteger()
        def action = (attrs.action ? attrs.action : (params.action ? params.action : "list"))
        def offset = params.offset?.toInteger()
        def max = params.max?.toInteger()
        def maxsteps = (attrs.maxsteps ? attrs.maxsteps.toInteger() : 10)

        if (!offset) offset = (attrs.offset ? attrs.offset.toInteger() : 0)
        if (!max) max = (attrs.max ? attrs.max.toInteger() : 10)
        if(total<=max){
            out<<"";
            return;
        }

        def writer = out
        writer<< '<div class="pagination pagination-centered"><ul>'
        if (attrs.total == null)
            throwTagError("Tag [paginate] is missing required attribute [total]")

        def messageSource = grailsAttributes.getApplicationContext().getBean("messageSource")
        def locale = RequestContextUtils.getLocale(request)

        //def total = attrs.total.toInteger()
        //def action = (attrs.action ? attrs.action : (params.action ? params.action : "list"))
        //def offset = params.offset?.toInteger()
        //def max = params.max?.toInteger()
        //def maxsteps = (attrs.maxsteps ? attrs.maxsteps.toInteger() : 10)

        if (!offset) offset = (attrs.offset ? attrs.offset.toInteger() : 0)
        if (!max) max = (attrs.max ? attrs.max.toInteger() : 10)

        def linkParams = [offset: offset - max, max: max]
        if (params.sort) linkParams.sort = params.sort
        if (params.order) linkParams.order = params.order
        if (attrs.params) linkParams.putAll(attrs.params)

        def linkTagAttrs = [action: action]
        if (attrs.controller) {
            linkTagAttrs.controller = attrs.controller
        }
        if (attrs.id != null) {
            linkTagAttrs.id = attrs.id
        }
        linkTagAttrs.params = linkParams

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = (offset / max) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil(total / max))

        // display previous link when not on firststep
        if (currentstep > firststep) {

            linkParams.offset = offset - max
            writer <<'<li>'
            writer << link(linkTagAttrs.clone()) {
                (attrs.prev ? attrs.prev : messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
            }
            writer << '</li>'
        }

        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {

            // determine begin and endstep paging variables
            int beginstep = currentstep - Math.round(maxsteps / 2) + (maxsteps % 2)
            int endstep = currentstep + Math.round(maxsteps / 2) - 1

            if (beginstep < firststep) {
                beginstep = firststep
                endstep = maxsteps
            }
            if (endstep > laststep) {
                beginstep = laststep - maxsteps + 1
                if (beginstep < firststep) {
                    beginstep = firststep
                }
                endstep = laststep
            }

            // display firststep link when beginstep is not firststep
            if (beginstep > firststep) {
                linkParams.offset = 0
                writer << '<li>'
                writer << link(linkTagAttrs.clone()) {firststep.toString()}
                writer << '</li>'
                writer << '<li class="disabled"><a href="#">...</a></li>'
            }

            // display paginate steps
            (beginstep..endstep).each { i ->
                if (currentstep == i) {
                    writer << "<li class=\"active\"><a href=\"#\">${i}</a></li>"
                }
                else {
                    linkParams.offset = (i - 1) * max
                    writer << '<li>'
                    writer << link(linkTagAttrs.clone()) {i.toString()}
                    writer << '</li>'
                }
            }

            // display laststep link when endstep is not laststep
            if (endstep < laststep) {
                writer <<'<li class="disabled"><a href="#">...</a></li>'
                linkParams.offset = (laststep - 1) * max
                writer << '<li>'
                writer << link(linkTagAttrs.clone()) { laststep.toString() }
                writer << '</li>'
            }
        }

        // display next link when not on laststep
        if (currentstep < laststep) {
            linkParams.offset = offset + max
            writer << '<li>'
            writer << link(linkTagAttrs.clone()) {
                (attrs.next ? attrs.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            }
            writer << '</li>'
        }

        writer<<"</ul></div>"
    }
}
