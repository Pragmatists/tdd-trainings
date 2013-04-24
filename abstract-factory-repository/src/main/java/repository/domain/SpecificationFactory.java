package repository.domain;

public interface SpecificationFactory {

    public Specification any();

    public Specification equalTo(final String property, final Object requiredValue);

    public <T> Specification greaterThan(final String property, final Comparable<T> expectedAtLeast);

    public Specification and(final Specification... specifications);

}