package com.thalesgroup.jenkins.plugins.initscript;

import hudson.Extension;
import hudson.model.Node;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class GroovyInitScript extends NodeProperty<Node> {

    private final String groovyCode;

    @DataBoundConstructor
    public GroovyInitScript(String groovyCode) {
        this.groovyCode = groovyCode;
    }

    public String getGroovyCode() {
        return groovyCode;
    }

    @Extension
    public static class DescriptorImpl extends NodePropertyDescriptor {

        @Override
        public NodeProperty<?> newInstance(StaplerRequest req, JSONObject formData)
                throws FormException
        {
            String groovyCode = req.getParameter("groovyCode");
            return new GroovyInitScript(groovyCode);
        }

        @Override
        public String getDisplayName() {
            return "Execute Groovy commands at Node startup";
        }

    }

}
