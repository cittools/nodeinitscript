package com.thalesgroup.jenkins.plugins.initscript;

import hudson.Extension;
import hudson.model.TaskListener;
import hudson.model.Computer;
import hudson.model.Descriptor;
import hudson.model.Node;
import hudson.slaves.ComputerListener;
import hudson.slaves.NodeProperty;
import hudson.slaves.NodePropertyDescriptor;
import hudson.util.DescribableList;
import hudson.util.RemotingDiagnostics;

import java.io.IOException;
import java.util.logging.Logger;

import jenkins.model.Jenkins;

@Extension
public class NodeInitListener extends ComputerListener {

    private static final Logger LOG = Logger.getLogger("[nodeinitscript]");
    
    
    @Override
    public void onOnline(Computer c, TaskListener listener) throws IOException,
            InterruptedException
    {
        Node node = c.getNode();

        Descriptor<?> descriptor = Jenkins.getInstance().getDescriptor(GroovyInitScript.class);

        if (descriptor != null && descriptor instanceof NodePropertyDescriptor) {

            DescribableList<NodeProperty<?>, NodePropertyDescriptor> nodeProperties;
            if (node instanceof Jenkins) {
                nodeProperties = ((Jenkins) node).getGlobalNodeProperties();
            } else {
                nodeProperties = node.getNodeProperties();
            }

            NodeProperty<?> nodeProperty = nodeProperties.get((NodePropertyDescriptor) descriptor);
            if (nodeProperty != null && nodeProperty instanceof GroovyInitScript) {
                String nodeName = "".equals(node.getNodeName()) ? "MASTER" : node.getNodeName();
                String code = ((GroovyInitScript) nodeProperty).getGroovyCode();
                String msg = "Executing groovy code on node " + nodeName + ":\n" + code;
                LOG.info(msg);
                listener.getLogger().println(msg);
                String result = RemotingDiagnostics.executeGroovy(code, c.getChannel());
                String msg2 = "Code result:\n" + result;
                LOG.info(msg2);
                listener.getLogger().println(msg2);
            }
        }
    }

}
