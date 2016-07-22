package sandbox;
import java.security.Permission;
public class SandboxSecurityManager extends SecurityManager{

        private static final SandboxSecurityManagerToggler TOGGLE_PERMISSION = new SandboxSecurityManagerToggler();
        ThreadLocal<Boolean> enabledFlag = null;
        public SandboxSecurityManager(final boolean enabledByDefault) {

            enabledFlag = new ThreadLocal<Boolean>() {

                @Override
                protected Boolean initialValue() {
                    return enabledByDefault;
                }

                @Override
                public void set(Boolean value) {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkPermission(TOGGLE_PERMISSION);
                    }
                    super.set(value);
                }
            };
        }

    @Override
    public void checkRead(String file) {
            super.checkRead(file);
    }

    @Override
        public void checkPermission(Permission permission) {
            if (shouldCheck(permission)) {
                super.checkPermission(permission);
            }
        }

        @Override
        public void checkPermission(Permission permission, Object context) {
            if (shouldCheck(permission)) {
                super.checkPermission(permission, context);
            }
        }

        private boolean shouldCheck(Permission permission) {
            return isEnabled() || permission instanceof SandboxSecurityManagerToggler;
        }

        public void enable() {
            enabledFlag.set(true);
        }

        public void disable() {
            enabledFlag.set(false);
        }

        public boolean isEnabled() {
            return enabledFlag.get();
        }
}
