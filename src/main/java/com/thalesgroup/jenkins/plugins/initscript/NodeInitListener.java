/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (c) 2012 Thales Global Services                                     *
 * Author : Robin Jarry                                                          *
 *                                                                               *
 * Permission is hereby granted, free of charge, to any person obtaining a copy  *
 * of this software and associated documentation files (the "Software"), to deal *
 * in the Software without restriction, including without limitation the rights  *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell     *
 * copies of the Software, and to permit persons to whom the Software is         *
 * furnished to do so, subject to the following conditions:                      *
 *                                                                               *
 * The above copyright notice and this permission notice shall be included in    *
 * all copies or substantial portions of the Software.                           *
 *                                                                               *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR    *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,      *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE   *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER        *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN     *
 * THE SOFTWARE.                                                                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
