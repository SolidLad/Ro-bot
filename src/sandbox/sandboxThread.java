package sandbox;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class SandboxThread extends Thread {
        private MessageReceivedEvent event;
        private String[] args;
        private SandboxClassLoader loader = new SandboxClassLoader();
        private SandboxSecurityManager sm = new SandboxSecurityManager(true);
        public SandboxThread(MessageReceivedEvent event, String[]args){
            this.event = event;
            this.args = args;
        }
        public void run() {
            SecurityManager old = System.getSecurityManager();
            System.setSecurityManager(sm);
            runUntrustedCode(args[0]);
            sm.disable();
            System.setSecurityManager(old);
        }
        private void runUntrustedCode(String commandName) {
            try {
                loader.loadClass(commandName)
                        .getMethod("run")
                        .invoke(null, event, args);
            } catch (Throwable t) {}
        }
}
