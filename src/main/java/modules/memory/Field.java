package modules.memory;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.RegEx;
import java.util.Objects;

// TODO: Debug logging

/**
 * The {@code Field} class is a class that represents the Fields in {@link
 * modules.memory.CustomMemory}.
 * These Fields do not hold values, but are rather used as templates for the Fields in an
 * {@link modules.memory.Entity}. {@link modules.memory.EntityField} is a Field that
 * inherits from {@code Field} and holds values.
 * <p>
 * {@link modules.memory.Field} and all the other components of this library
 * provide a wide check of edge-points and invalid inputs, however, there is no
 * guarantee that invalid inputs will necessarily throw exceptions. It is up the
 * developer to validate and ensure functionality of their inputs and any external
 * implementations.
 *
 * @apiNote It is important to note that the clear separation between {@code Field} and
 *        {@link modules.memory.EntityField} should be - and is - kept, both in
 *        {@link modules.memory.CustomMemory} and in other implementations.
 * 		<p>
 * 		Fields are split into two types: {@code Headers} and {@code Inner-Fields}.
 * 		Headers are supposed to hold unique values per
 *        {@link modules.memory.Entity} in order to segregate between {@code Entities}
 * 		and make it possible to access them individually. Headers, therefore,
 * 		hold constant and non-{@link #nullable} values and their {@link #defaultValue}
 * 		is {@code null}.
 * 		<p>
 * 		Inner-Fields, however, are far more fluid: while their names and
 * 		attributes remain constant, you can set whether they're {@code nullable} and
 * 		their default value (attributes that are pre-set in Headers),
 * 		as well as change their value in {@code Entities}.
 * 		<p>
 * 		The names and orders of all Fields in a {@link modules.memory.CustomMemory}
 * 		have to be unique, regardless of their type.
 * @see modules.memory.CustomMemory
 * @see modules.memory.EntityField
 * @see modules.memory.Entity
 */

@SuppressWarnings("unused")
public class Field {
	/**
	 * The order number required in order to consider a Field as a Header.
	 *
	 * @see #order
	 */
	
	static final int HEADER_ORDER = 0;
	
	private static final String NULLABLE_HEADER = "Headers cannot be nullable.";
	private static final String INVALID_ORDER =
			"The order must be greater than or equal to " + Field.HEADER_ORDER;
	private static final String INVALID_REGEX =
			"The regex must not be empty and must " + "not include ':'.";
	private static final String INVALID_DEFAULT_VALUE =
			"Default value must match the regex.";
	private static final String DEFAULT_HEADER = "Headers cannot have default values.";
	private static final String NON_NULLABLE_DEFAULT =
			"The default value cannot be empty if nullable is false.";
	private static final String INVALID_NAME =
			"The name cannot be empty or contain " + "':'.";
	
	/**
	 * The name of the Field.
	 *
	 * @apiNote The name of the Field can contain any ASCII-supported characters
	 * 		(including white-space),
	 * 		except for {@code ':'}. The colon character is not supported due to it being
	 * 		used to separate the Field's name from the Field's value.
	 * 		<p>
	 * 		Two or more Fields of the same {@link modules.memory.CustomMemory} -
	 * 		ultimately of the same {@link modules.memory.Entity} - cannot have the same
	 * 		name.
	 */
	
	@NotNull protected final String name;
	
	/**
	 * The regex pattern the {@code Field}'s {@code value} should follow when used in an
	 * {@link modules.memory.Entity}.
	 * This pattern applies both to values inserted into the field and the {@link
	 * #defaultValue}, as long as it is not empty.
	 *
	 * @apiNote The regex cannot be {@code null}, nor can it include a raw usage of
	 *        {@code ':'}. The regex also has to be valid.
	 * @see javax.annotation.RegEx
	 * @see #defaultValue
	 */
	
	@Language("JSRegexp")
	@RegEx
	@NotNull
	protected final String regex;
	
	/**
	 * The order of the {@code Field} in the Entity. The {@link java.util.List} of {@code
	 * Field}s in {@link modules.memory.CustomMemory} and {@link modules.memory.Entity}
	 * is sorted by this attribute, as well as the Entity's text representation itself.
	 * <p>
	 * If the order is {@code 0}, it will be considered a Header.
	 *
	 * @apiNote Any {@code order} below {@code 0} is invalid. Note that when setting
	 * 		the {@code order} to {@code 0}, the limitations of Header will apply.
	 * 		<p>
	 * 		Two or more Fields of the same {@link modules.memory.CustomMemory} -
	 * 		ultimately of the same {@link modules.memory.Entity} - cannot have the
	 * 		same order.
	 */
	
	protected final int order;
	
	/**
	 * The default value of a Field. This value is used when an
	 * {@link modules.memory.EntityField} doesn't have a value.
	 * <p>
	 * This attribute is {@code null} by default.
	 *
	 * @apiNote This attribute is {@code Nullable} in order to signify when a Field
	 * 		doesn't have a default value, at all.
	 * 		However, if the default value is <i>defined</i> as literally empty, you
	 * 		should use {@code ""} instead.
	 * 		<p>
	 * 		It is important to note that {@code null} default values oblige the value to
	 * 		always be non-{@code null}, since a {@code null} value is translated to the
	 *        {@code defaultValue}.
	 * 		This is not to be confused with an empty value ({@code ""}) that, given that
	 * 		the {@link #nullable} attribute is true, is valid and will not change forms.
	 * 		<p>
	 * 		The default value should also match the {@link #regex}, if not empty.
	 * @see #nullable
	 * @see #regex
	 */
	
	@Nullable protected final String defaultValue;
	
	/**
	 * The {@code nullable} attribute determines whether a value can be empty ({@code
	 * ""})
	 * or not.
	 * This is not to be confused with a {@code null} value, which the fate of is
	 * determined by {@link #defaultValue}.
	 * <p>
	 * This attribute is {@code true} by default.
	 *
	 * @apiNote A value cannot be empty, even if it matches the {@link #regex}, if this
	 * 		attribute is {@code false}.
	 * 		However, a value can be empty, regardless of the {@link #regex}, if this
	 * 		attribute is true.
	 * @see #regex
	 * @see #defaultValue
	 */
	
	protected final boolean nullable;
	
	/**
	 * Initializes a new {@code Field} that consists of a {@code name}, a {@code regex}
	 * and a {@code order}.
	 * <p>
	 * This constructor is <b>not</b> for Headers, but for Inner-Fields. The Header
	 * constructor is {@link #Field(String, String, int, String, boolean)}.
	 *
	 * @param name  the name of the field
	 * @param regex the regex of the field
	 * @param order the order of the field
	 *
	 * @throws java.lang.IllegalArgumentException If {@code name}, {@code regex},
	 *                                            {@code order} or {@code defaultValue}
	 *                                            are invalid, or if the Field is a
	 *                                            Header.
	 * @implNote If {@code regex} is {@code null}, the {@link #regex} attribute will be
	 * 		set to {@code "^.+$"}.
	 * 		<p>
	 *        {@link #defaultValue} is set to {@code null}, and {@link #nullable} is
	 * 		set to {@code true}.
	 * @see #name
	 * @see #regex
	 * @see #order
	 */
	
	@NotNull
	public Field (@NotNull String name, @Nullable String regex, int order) {
		if (order < Field.HEADER_ORDER)
			throw new IllegalArgumentException(Field.INVALID_ORDER);
		
		if (name.isEmpty() || name.contains(":"))
			throw new IllegalArgumentException(Field.INVALID_NAME);
		
		if (regex != null && (regex.isEmpty() || regex.contains(":")))
			throw new IllegalArgumentException(Field.INVALID_REGEX);
		
		if (order == 0 && isNullable())
			throw new IllegalArgumentException(NULLABLE_HEADER);
		
		this.name         = name;
		this.regex        = regex != null ? regex : "^.+$";
		this.order        = order;
		this.defaultValue = null;
		this.nullable     = true;
	}
	
	/**
	 * Initializes a new {@code Field} that, along with the basic attributes, also has
	 * a defaultValue.
	 * <p>
	 * This constructor is <b>not</b> for Headers, but for Inner-Fields. The Header
	 * constructor is {@link #Field(String, String, int, String, boolean)}.
	 *
	 * @param name         the name of the field
	 * @param regex        the regex of the field
	 * @param order        the order of the field
	 * @param defaultValue the default value of the field
	 *
	 * @throws java.lang.IllegalArgumentException If {@code name}, {@code regex},
	 *                                            {@code order} or {@code defaultValue}
	 *                                            are invalid, or if the Field is a
	 *                                            Header.
	 * @implNote If {@code regex} is {@code null}, the {@link #regex} attribute will be
	 * 		set to {@code "^.+$"}, and {@link #nullable} is set to {@code true}.
	 * @see #name
	 * @see #regex
	 * @see #order
	 * @see #defaultValue
	 */
	
	@NotNull
	public Field (@NotNull String name, @Nullable String regex, int order,
	              @Nullable String defaultValue) {
		
		if (order < Field.HEADER_ORDER)
			throw new IllegalArgumentException(Field.INVALID_ORDER);
		
		if (name.isEmpty() || name.contains(":"))
			throw new IllegalArgumentException(Field.INVALID_NAME);
		
		if (regex != null && (regex.isEmpty() || regex.contains(":")))
			throw new IllegalArgumentException(Field.INVALID_REGEX);
		
		if (order == Field.HEADER_ORDER && defaultValue != null)
			throw new IllegalArgumentException(Field.DEFAULT_HEADER);
		
		this.name  = name;
		this.regex = regex != null ? regex : "^.+$";
		
		if (defaultValue != null && !defaultValue.isEmpty() && !defaultValue.matches(
				this.regex))
			throw new IllegalArgumentException(Field.INVALID_DEFAULT_VALUE);
		
		this.order        = order;
		this.defaultValue = defaultValue;
		this.nullable     = true;
	}
	
	/**
	 * Initializes a new {@code Field} that sets all attributes.
	 * <p>
	 * This constructor shall be used when initializing Headers, but may also be used
	 * to initialize Inner-Fields.
	 *
	 * @param name         the name of the field
	 * @param regex        the regex of the field
	 * @param order        the order of the field
	 * @param defaultValue the default value of the field
	 * @param nullable     whether the value is allowed to be empty or not
	 *
	 * @throws java.lang.IllegalArgumentException If the {@code name}, {@code regex},
	 *                                            {@code order}, {@code defaultValue} or
	 *                                            {@code nullable} are invalid.
	 * @implNote If {@code regex} is {@code null}, the {@link #regex} attribute will be
	 * 		set to {@code "^.+$"}.
	 * @see #name
	 * @see #regex
	 * @see #order
	 * @see #defaultValue
	 * @see #nullable
	 */
	
	public Field (@NotNull String name, @Nullable String regex, int order,
	              @Nullable String defaultValue, boolean nullable) {
		if (order < Field.HEADER_ORDER)
			throw new IllegalArgumentException(Field.INVALID_ORDER);
		
		if (name.isEmpty() || name.contains(":"))
			throw new IllegalArgumentException(Field.INVALID_NAME);
		
		if (regex != null && (regex.isEmpty() || regex.contains(":")))
			throw new IllegalArgumentException(Field.INVALID_REGEX);
		
		if (order == Field.HEADER_ORDER && nullable)
			throw new IllegalArgumentException(Field.NULLABLE_HEADER);
		
		if (order == Field.HEADER_ORDER && defaultValue != null)
			throw new IllegalArgumentException(Field.DEFAULT_HEADER);
		
		if (defaultValue != null && defaultValue.isEmpty() && !nullable)
			throw new IllegalArgumentException(Field.NON_NULLABLE_DEFAULT);
		
		this.name  = name;
		this.regex = regex != null ? regex : "^.+$";
		
		if (defaultValue != null && !defaultValue.isEmpty() && !defaultValue.matches(
				this.regex))
			throw new IllegalArgumentException(Field.INVALID_DEFAULT_VALUE);
		
		this.order        = order;
		this.defaultValue = defaultValue;
		this.nullable     = nullable;
	}
	
	/**
	 * A copy-constructor that copies all the attributes of another field.
	 *
	 * @param field the other field.
	 */
	
	public Field (Field field) {
		this.name         = field.name;
		this.regex        = field.regex;
		this.order        = field.order;
		this.defaultValue = field.defaultValue;
		this.nullable     = field.nullable;
	}
	
	/**
	 * Returns the {@link #name} of the field.
	 *
	 * @return the {@link #name} of the field.
	 */
	
	@NotNull
	public String getName () {
		return this.name;
	}
	
	/**
	 * Returns the {@link #regex} of the field.
	 *
	 * @return the {@link #regex} of the field.
	 */
	
	@NotNull
	public String getRegex () {
		return this.regex;
	}
	
	/**
	 * Returns the {@link #order} of the field.
	 *
	 * @return the {@link #order} of the field.
	 */
	
	public int getOrder () {
		return this.order;
	}
	
	/**
	 * Returns the {@link #defaultValue} of the field.
	 *
	 * @return the {@link #defaultValue} of the field.
	 */
	
	@Nullable
	public String getDefaultValue () {
		return this.defaultValue;
	}
	
	/**
	 * Returns {@code true} if {@link #nullable} is true, otherwise false.
	 *
	 * @return {@code true} if {@link #nullable} is true, otherwise false.
	 */
	
	public boolean isNullable () {
		return nullable;
	}
	
	/**
	 * Returns whether this field is a Header or not.
	 *
	 * @return whether this field is a Header or not.
	 */
	
	public boolean isHeader () {
		return this.order == Field.HEADER_ORDER;
	}
	
	/**
	 * Returns whether this field has a defined {@link #defaultValue}.
	 *
	 * @return whether this field has a defined {@link #defaultValue}.
	 */
	
	public boolean isDefaultValue () {
		return defaultValue != null;
	}
	
	/**
	 * Returns true if {@code other} is a {@code Field} and if all the attributes of this
	 * {@code Field} and another {@code Object}'s attributes are the same, otherwise
	 * false.
	 *
	 * @param other the object to compare this field to.
	 *
	 * @return true if {@code other} is a {@code Field} and if all the attributes of this
	 *        {@code Field} and another {@code Object}'s attributes are the same,
	 * 		otherwise false.
	 *
	 * @implNote Does not include value comparison.
	 */
	
	@Contract(value = "null -> false", pure = true)
	@Override
	public boolean equals (Object other) {
		return other instanceof Field &&
		       this.name.equals(((Field) other).name) &&
		       this.regex.equals(((Field) other).regex) &&
		       this.order == ((Field) other).order &&
		       Objects.equals(this.defaultValue, ((Field) other).defaultValue) &&
		       this.nullable == ((Field) other).nullable;
	}
}
