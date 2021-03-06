// Copyright © 2008-2010 Esko Luontola <www.orfjackal.net>
// This software is released under the Apache License 2.0.
// The license text is at http://dimdwarf.sourceforge.net/LICENSE

package net.orfjackal.dimdwarf.actors;

public class HideInterruptedExceptions implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof InterruptedException) {
            return;
        }
        System.err.printf("Exception in thread \"%s\" ", t.getName());
        e.printStackTrace();
    }
}
