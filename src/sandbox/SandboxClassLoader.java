package sandbox;

public class SandboxClassLoader extends ClassLoader{
        private Class[] whitelist = {commands.utils.Command.class};
        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            for (int i = 0; i < whitelist.length; i++) {
                if (name.equals(whitelist[i].getName()))
                    return super.loadClass(name);
            }
            return findClass(name);
        }
        @Override
        public Class findClass(String name) {
            byte[] b = loadClassData(name);
            return defineClass(name, b, 0, b.length);
        }
        private byte[] loadClassData(String name) {
            // load the untrusted class data here
            return null;
        }
}
