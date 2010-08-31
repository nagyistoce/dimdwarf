// Copyright © 2008-2010 Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://dimdwarf.sourceforge.net/LICENSE

package net.orfjackal.dimdwarf.modules;

import com.google.inject.*;
import net.orfjackal.dimdwarf.auth.*;
import net.orfjackal.dimdwarf.mq.MessageReceiver;
import net.orfjackal.dimdwarf.services.*;

public class AuthenticatorModule extends ServiceModule {

    public AuthenticatorModule() {
        super("Authenticator");
    }

    protected void configure() {
        bindControllerTo(AuthenticatorController.class);
        bindServiceTo(AuthenticatorService.class);
        bindMessageQueueOfType(Object.class);

        bind(Authenticator.class).to(AuthenticatorController.class);
        expose(Authenticator.class);

        bind(new TypeLiteral<CredentialsChecker<Credentials>>() {}).toInstance(new CredentialsChecker<Credentials>() {
            public boolean isValid(Credentials credentials) {
                return false; // TODO
            }
        });
    }

    @Provides
    ServiceRunnable service(Service service, MessageReceiver<Object> toService) {
        return new ServiceMessageLoop(service, toService);
    }
}
