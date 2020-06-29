package modules.memory;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

// TODO: Debug logging

/**
 * The {@code EntityField} class is an extension of {@link modules.memory.Field} that
 * holds a value.
 * This field is used as a key-value object inside Entities.
 * <p>
 * This class requires a {@link modules.memory.Field} in all its constructors, as it is
 * an "instance" of a pre-set Field that holds a value.
 * Thus, all the arguments of the provided {@link modules.memory.Field} apply to the
 * value.
 *
 * @apiNote Headers have a constant value that cannot be changed. You can bypass this
 * 		by creating a new {@link modules.memory.Entity} with a new Header and copying the
 * 		values to it.
 * @see modules.memory.CustomMemory
 * @see modules.memory.Entity
 * @see modules.memory.Field
 */

@SuppressWarnings("unused")
public class EntityField extends Field {
	private static final String NON_NULLABLE_VALUE =
			"Cannot set the value of a non-nullable field to empty.";
	private static final String NON_DEFAULT_VALUE = "Cannot set the value of a field " +
	                                                "with an undefined default value " +
	                                                "to" +
	                                                " the default value.";
	
	/**
	 * The value of the field in a particular {@link modules.memory.Entity}.
	 *
	 * @apiNote This value must abide by the rules set in the
	 *        {@link modules.memory.Field} provided in the constructor and detailed in
	 * 		the
	 *        {@link modules.memory.Field}'s JavaDoc.
	 * 		<p>
	 * 		The value, if not null or empty, must match the pre-defined {@code regex}.
	 * 		The value cannot be empty if {@code nullable} is set to {@code false}.
	 * 		The value cannot be null if {@code defaultValue} is {@code null}.
	 * @see modules.memory.Field
	 * @see modules.memory.Field#defaultValue
	 * @see modules.memory.Field#nullable
	 * @see modules.memory.Field#order
	 * @see modules.memory.Field#regex
	 */
	
	@SuppressWarnings("NotNullFieldNotInitialized")
	@NotNull
	private String value;
	
	/**
	 * Initializes the {@code EntityField} with a {@link modules.memory.Field} and a null
	 * value.
	 *
	 * @param field the {@link modules.memory.Field} this {@code EntityField} is an
	 *              instance of.
	 *
	 * @throws java.lang.IllegalArgumentException if the {@code value} cannot be
	 *                                            {@code null}.
	 */
	
	@NotNull
	public EntityField (@NotNull Field field) {
		super(field.name, field.regex, field.order, field.defaultValue, field.nullable);
		validateValue(field);
	}
	
	/**
	 * Initializes the {@code EntityField} with a {@link modules.memory.Field} and a
	 * value.
	 *
	 * @param field the {@link modules.memory.Field} this {@code EntityField} is an
	 *              instance of.
	 * @param value the value.
	 *
	 * @throws java.lang.IllegalArgumentException if the {@code value} is invalid.
	 */
	
	@NotNull
	public EntityField (@NotNull Field field, @Nullable String value) {
		super(field.name, field.regex, field.order, field.defaultValue, field.nullable);
		validateValue(field, value);
	}
	
	/**
	 * Validates the value by checking it in accordance to the
	 * {@link modules.memory.Field}'s arguments.
	 *
	 * @param field the {@link modules.memory.Field} this {@code EntityField} is an
	 *              instance of, or the {@link modules.memory.EntityField} to check.
	 *
	 * @throws java.lang.IllegalArgumentException if the {@code value} is invalid.
	 */
	
	protected final void validateValue (@NotNull Field field) {
		String val = field instanceof EntityField ? ((EntityField) field).value : null;
		validateValue(field, val);
	}
	
	/**
	 * @param field the {@link modules.memory.Field} this {@code EntityField} is an
	 *              instance of.
	 * @param value the value of this {@code EntityField}.
	 *
	 * @throws java.lang.IllegalArgumentException if the {@code value} is invalid.
	 */
	
	protected final void validateValue (@NotNull Field field, @Nullable String value) {
		
		if (value == null) {
			if (field.isDefaultValue())
				throw new IllegalArgumentException(EntityField.NON_DEFAULT_VALUE);
			this.value = Objects.requireNonNull(field.defaultValue);
		} else if (value.isEmpty()) {
			if (!nullable)
				throw new IllegalArgumentException(EntityField.NON_NULLABLE_VALUE);
			this.value = "";
		} else {
			if (!value.matches(regex))
				throw new IllegalArgumentException("Value " +
				                                   value +
				                                   " doesn't match " +
				                                   "field's regex (" +
				                                   field.regex +
				                                   ").");
			this.value = value;
		}
	}
	
	/**
	 * A method that compares between this {@code EntityField} and a
	 * {@link java.lang.Object}.
	 *
	 * @param other the object to compare this field to.
	 *
	 * @return true if the all the arguments and the {@code value} are equal, otherwise
	 * 		false.
	 */
	
	@Contract(value = "null -> false", pure = true)
	@Override
	public boolean equals (@Nullable Object other) {
		return other instanceof Field &&
		       this.name.equals(((Field) other).name) &&
		       this.regex.equals(((Field) other).regex) &&
		       this.order == ((Field) other).order &&
		       Objects.equals(this.defaultValue, ((Field) other).defaultValue) &&
		       this.nullable == ((Field) other).nullable &&
		       (other instanceof EntityField && Objects.equals(this.value,
		                                                       ((EntityField) other).value));
	}
	
	/**
	 * Returns the {@code value}.
	 *
	 * @return the {@link modules.memory.EntityField#value}.
	 */
	
	@Contract(pure = true)
	@NotNull
	public final String getValue () {
		return value;
	}
	
	/**
	 * Sets the the {@link modules.memory.EntityField#value} to a new {@code value}.
	 *
	 * @param value the value to set the {@link modules.memory.EntityField#value} to.
	 *
	 * @throws java.lang.IllegalArgumentException if the {@code value} is invalid.
	 */
	
	public final void setValue (@Nullable String value) {
		validateValue(this, value);
	}
	
	/**
	 * Returns true if the value is empty, but not {@code null}, otherwise false.
	 *
	 * @return true if the value is empty, but not {@code null}, otherwise false.
	 */
	
	@Contract(pure = true)
	public boolean isEmpty () {
		return this.value.equals("");
	}
	
	/**
	 * Returns a String representation of this {@code EntityField}.
	 *
	 * @return a String representation of this {@code EntityField}.
	 */
	
	@Contract(pure = true)
	@Override
	@NotNull
	public String toString () {
		return "(" + this.name + ", " + this.value + ")";
	}
	
	/**
	 * Returns true if the {@link modules.memory.EntityField#value} equals to the
	 * {@link modules.memory.Field#defaultValue}, otherwise false.
	 *
	 * @return true if the {@link modules.memory.EntityField#value} equals to the
	 *        {@link modules.memory.Field#defaultValue}, otherwise false.
	 */
	
	@Contract(pure = true)
	public boolean isValueDefault () {
		return this.value.equals(this.defaultValue);
	}
}
