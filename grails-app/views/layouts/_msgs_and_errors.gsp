<g:if test="${errors != null}">
    <div class="alert alert-error" STYLE="color: RED">
        ${errors}
    </div>
</g:if>
<g:if test="${msgs}">
    <div class="alert alert-info">
        ${msgs}
    </div>
</g:if>
<g:if test="${flash.message}">
    <div class="alert alert-info">
        ${flash.message}
    </div>
</g:if>
<g:if test="${flash.errors != null}">
    <div class="alert alert-error" STYLE="color: RED">
        ${flash.errors}
    </div>
</g:if>