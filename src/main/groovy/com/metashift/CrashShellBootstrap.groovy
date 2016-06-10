package com.metashift

import com.metashift.context.FileableContext
import com.metashift.crash.BetterBootstrap
import com.metashift.crash.ShellWithNoise
import org.crsh.console.jline.JLineProcessor
import org.crsh.console.jline.Terminal
import org.crsh.console.jline.TerminalFactory
import org.crsh.console.jline.console.ConsoleReader
import org.crsh.console.jline.internal.Configuration
import org.crsh.shell.Shell
import org.crsh.shell.ShellFactory
import org.crsh.util.InterruptHandler
import org.fusesource.jansi.AnsiConsole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
/**
 * Created by navid on 1/16/15.
 */
@Profile("crash")
@Component
@DependsOn(['crashBootstrap'])
class CrashShellBootstrap implements CommandLineRunner {


    @Autowired
    private BetterBootstrap bootstrap

    @Autowired
    private FileableContext fileableContext

    @Override
    void run(String... args) throws Exception {

        ShellFactory factory = bootstrap.getContext().getPlugin(ShellFactory.class);
        Shell shell = factory.create(null);

        if (shell != null) {
            /* Proxy this sucker so we can have an easier way to inject noise */
            shell = new ShellWithNoise(shell,fileableContext)

            final Terminal term = TerminalFactory.create();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        term.restore();
                    }
                    catch (Exception ignore) {
                    }
                }
            });

            //
            String encoding = Configuration.getEncoding();

            // Use AnsiConsole only if term doesn't support Ansi
            PrintStream out;
            PrintStream err;
            boolean ansi;
            if (term.isAnsiSupported()) {
                out = new PrintStream(new BufferedOutputStream(term.wrapOutIfNeeded(new FileOutputStream(FileDescriptor.out)), 16384), false, encoding);
                err = new PrintStream(new BufferedOutputStream(term.wrapOutIfNeeded(new FileOutputStream(FileDescriptor.err)), 16384), false, encoding);
                ansi = true;
            } else {
                out = AnsiConsole.out;
                err = AnsiConsole.err;
                ansi = false;
            }

            //
            FileInputStream fis = new FileInputStream(FileDescriptor.in);
            ConsoleReader reader = new ConsoleReader(null, fis, out, term);

            //
            final JLineProcessor processor = new JLineProcessor(ansi, shell, reader, out);

            //
            InterruptHandler interruptHandler = new InterruptHandler(new Runnable() {
                @Override
                public void run() {
                    processor.interrupt();
                }
            });
            interruptHandler.install();

            //
            Thread thread = new Thread(processor);
            thread.setDaemon(true);
            thread.start();

            //
            try {
                processor.closed();
            }
            catch (Throwable t) {
                t.printStackTrace();
            } finally {

                // Force exit
                System.exit(0);
            }

        }
    }
}

