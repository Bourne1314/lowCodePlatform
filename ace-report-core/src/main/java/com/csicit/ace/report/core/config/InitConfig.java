package com.csicit.ace.report.core.config;

import com.stimulsoft.base.licenses.StiLicense;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author shanwj
 * @date 2019/10/24 8:16
 */
@Component
@Order(100)
public class InitConfig implements ApplicationRunner {
    private final static String LicenseKey =
            "6vJhGtLLLz2GNviWmUTrhSqnOItdDwjBylQzQcAOiHnVc0bgkfvM4lNZJRX9Z35qWUnVViOMwQskMki/GvdfGjaLEA" +
                    "lTposilduwIhoMqTID58gPpZp4O4RvZC3BTEEYoiBkV74meZPmqGkXAefLF2/RXni8Ah6p8Vzbaks83CeVBzveVDfq" +
                    "ISePbHRgnkQxBNaq9nYDwYR7eA140ipFvA2/6TWsvRr2bdjCL0V+rtuaQm+wFrTPl9u3TP7qIQxaMm4Ub5P0czFD1K" +
                    "jxtaMSN47wkHGgP/NaLiKJN6XcYYsAn02PBGDXVPA9KSE4WFdrhuGRhrCIFsGQcaCC0ZvHq10VX8pUdP1a2IWZBM9m" +
                    "Kg73JyyzH3k5rJn4fLPVxQGBCGq/8oVGuzRi69zDFYC4DhKnr4rMvrnSLsG4vAH7QSri104O4gL+V9LIBWXDG1cKuT" +
                    "99HW6FBeQmjwzA9SythwT+Sz0P+8URaN1hYEMT8dHctDQaAcQ9UC2/fOKTqnVwEK97F2TWr5c6OpLs3RAEErcab9tU" +
                    "2L+Fj8BXJj4+v4fkDH4vI+sC8DYHrGvw77ZRiNGYMGcMQjXUM9/v66X7Pm5sUXEaFHvturkwXaqkVs/sFyQOS9XzK4" +
                    "Ct9xtYiwau6jtUSrsdzKvcrKu0oZITBYygLPVwJablD04osIRG78bb7FfoO+7rMdoY/y96ujX+";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StiLicense.setKey(LicenseKey);
    }
}
