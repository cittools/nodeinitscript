Jenkins Node Init Script Plugin
===============================

This plugin allows the execution of arbitrary Groovy code on Jenkins slaves (nodes)
when they come online.

This is often needed for preparing the environment of the node (startup of specific 
services, mounting of network shares, etc.)

The configuration can be done in the global settings of each node.

Example
-------

![Example configuration](https://cittools.github.com/nodeinitscript/screenshot.png)

### Output

    Executing groovy code on node slave1:
    println "Cleaning old drive mounts..."
    println "net use /d t:".execute().text
    println "net use /d r:".execute().text

    println "Mounting drives..."
    println "net use t: \\\\nas-server\\tools".execute().text
    println "net use r: \\\\nas-server\\releases".execute().text

    println "Mounting clearcase VOBs..."
    println "cleartool mount -all".execute().text
    Code result:
    Cleaning old drive mounts...
    r: was deleted successfully.
    s: was deleted successfully.

    Mounting drives...
    The command completed successfully.
    The command completed successfully.
    The command completed successfully.

    Mounting clearcase VOBs...
    Mounting MVFS filesystem \DataMgt_comp.
    Mounting MVFS filesystem \DeskMgt_comp.
    Mounting MVFS filesystem \DocGenMgt_comp.
    Mounting MVFS filesystem \IvvqMgt_comp.
    Mounting MVFS filesystem \LinkMgt_comp.
    ....
    Mounting MVFS filesystem \MDE_SOL.
    Mounting MVFS filesystem \MDE.
    Mounting MVFS filesystem \papeete.
    Mounting MVFS filesystem \P_WBS.

    Slave successfully connected and online
