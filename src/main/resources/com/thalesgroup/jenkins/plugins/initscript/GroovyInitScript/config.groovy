
import lib.LayoutTagLib


def l  = namespace(LayoutTagLib)
def t  = namespace("/lib/hudson")
def st = namespace("jelly:stapler")
def f  = namespace("lib/form")

f.entry(title: _("Groovy code"), field: "groovyCode") {
    
    // this text area is what we convert to the super text area
    // we use CSS class to hook up the initialization script. In this particular demo,
    // the ID attribute can be used, but in more general case (such as when you use this in your Builder, etc.,
    // a single web page may end up containing multiple instances of such text area, so the CSS class works better.
    f.textarea(id: "groovyCode", 
               name: "groovyCode", 
               "class": "groovy-code"
               //style: "width:100%; height:10em"
               ) {
        instance.groovyCode
    }
    
    // this loads the necessary JavaScripts, if it hasn't loaded already
    // the first we load is the mode definition file (mode as in Emacs mode)
    // the second is the theme.
    //
    // for other modes, look for "clike.js" in your IDE and see adjacent folders.
    st.adjunct(includes: "org.kohsuke.stapler.codemirror.mode.clike.clike")
    st.adjunct(includes: "org.kohsuke.stapler.codemirror.theme.default")
    
    // see CodeMirror web site for more about how to control the newly instantiated text area.
    script("""
hudsonRules["TEXTAREA.groovy-code"] = function(e) {
    var w = CodeMirror.fromTextArea(e,{
        mode:"text/x-groovy",
        lineNumbers: true
    }).getWrapperElement();
    w.setAttribute("style","border:1px solid black; margin-top: 1em; margin-bottom: 1em")
}
""")

}

