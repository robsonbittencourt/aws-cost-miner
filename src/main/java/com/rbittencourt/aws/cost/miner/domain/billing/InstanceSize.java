package com.rbittencourt.aws.cost.miner.domain.billing;

import java.math.BigDecimal;

import static java.util.Arrays.stream;

public enum InstanceSize {

    NANO("nano", new BigDecimal(0.25)),
    MICRO("micro", new BigDecimal(0.5)),
    SMALL("small", new BigDecimal(1)),
    MEDIUM("medium", new BigDecimal(2)),
    LARGE("large", new BigDecimal(4)),
    XLARGE("xlarge", new BigDecimal(8)),
    XLARGE2("2xlarge", new BigDecimal(16)),
    XLARGE4("4xlarge", new BigDecimal(32)),
    XLARGE8("8xlarge", new BigDecimal(64)),
    XLARGE9("9xlarge", new BigDecimal(72)),
    XLARGE10("10xlarge", new BigDecimal(80)),
    XLARGE16("16xlarge", new BigDecimal(128)),
    XLARGE24("24xlarge", new BigDecimal(192)),
    XLARGE32("32xlarge", new BigDecimal(256));

    InstanceSize(String description, BigDecimal normalizationFactor) {
        this.description = description;
        this.normalizationFactor = normalizationFactor;
    }

    private String description;
    private BigDecimal normalizationFactor;

    public static InstanceSize fromDescription(String description) {
        return stream(values())
                .filter(v -> v.description.equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid instance size: " + description));
    }

    public BigDecimal getNormalizationFactor() {
        return normalizationFactor;
    }

    public BigDecimal normalize(InstanceSize instanceSize) {
        BigDecimal originNormalizationFactor = this.getNormalizationFactor();
        BigDecimal targetNormalizationFactor = instanceSize.getNormalizationFactor();

        return originNormalizationFactor.divide(targetNormalizationFactor);
    }

}
