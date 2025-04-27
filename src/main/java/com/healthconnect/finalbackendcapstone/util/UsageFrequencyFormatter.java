package com.healthconnect.finalbackendcapstone.util;

import com.healthconnect.finalbackendcapstone.model.SubstanceUse;

public class UsageFrequencyFormatter {
    
    /**
     * Get a display-friendly version of the usage frequency
     * @param frequency the usage frequency enum value
     * @return a display-friendly string
     */
    public static String formatUsageFrequency(SubstanceUse.UsageFrequency frequency) {
        if (frequency == null) {
            return "";
        }
        
        switch (frequency) {
            case DAILY:
                return "Daily";
            case WEEKLY:
                return "Weekly";
            case MONTHLY:
                return "Monthly";
            case OCCASIONALLY:
                return "Occasionally";
            case RARELY:
                return "Rarely";
            case FORMER:
                return "Former use";
            default:
                return frequency.name();
        }
    }
} 