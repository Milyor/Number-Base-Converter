package converter // Do not delete this line
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import kotlin.math.pow
import java.math.RoundingMode

fun main() {

    print("\nEnter two numbers in format: {source base} {target base} (To quit type /exit)")
    var input = readln()
    while (input != "/exit") {
        val inputParts = input.split(" ")
        val sourceBase = inputParts[0].toInt()
        val targetBase = inputParts[1].toInt()
        print("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
        var number = readln()
        while (number != "/back") {
            val result = if (number.contains('.')){
                val sourceValue = number.split('.')
                val intPart = sourceValue[0]
                val fractionPart = sourceValue[1]
                "${ convertBase(intPart, sourceBase, targetBase) + "." +convertFraction(fractionPart, sourceBase, targetBase) }"
            } else {
                convertBase(number, sourceBase, targetBase)
            }
            println("Conversion result: $result")
            print("\nEnter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
            number = readln()
        }
        print("\nEnter two numbers in format: {source base} {target base} (To quit type /exit)")
        input = readln()
    }
}

fun convertFraction(source: String, sourceBase: Int, targetBase: Int): String {
    // Converting to decimal fraction
    var decimalValue = BigDecimal.ZERO
    var power = -1
    for (digit in source.indices) {
        val digitValue = Character.getNumericValue(source[digit])
        decimalValue += BigDecimal.valueOf(digitValue.toLong()).multiply(BigDecimal.valueOf(sourceBase.toLong()).pow(power, MathContext.DECIMAL64))
        power -= 1
    }
    // Convert from decimal to any other base
    val targetDigit = StringBuilder()
    val maxIterations = 5 // Set the desired precision

    for (i in 1..maxIterations) {
        decimalValue = decimalValue.remainder(BigDecimal.ONE)
        decimalValue *= BigDecimal.valueOf(targetBase.toLong())// Isolate fractional part
        val digit = decimalValue.toInt()
        targetDigit.append(digit.toString(targetBase).lowercase())
        decimalValue -= BigDecimal(digit)
    }
    return targetDigit.toString().uppercase()
}

fun convertBase(source: String, sourceBase: Int, targetBase: Int): String {

        // Convert source to number to base 10
        var decimalValue = BigInteger.ZERO
        var sourceBigInt = BigInteger(source.uppercase(), sourceBase)

        for (i in source.indices) {
            val digit = sourceBigInt % BigInteger.valueOf(sourceBase.toLong())
            decimalValue += digit * BigInteger.valueOf(sourceBase.toLong()).pow(i)
            sourceBigInt /= BigInteger.valueOf(sourceBase.toLong())
        }
        // Convert to base 10 to the target base
        val targetDigits = StringBuilder()

        while (decimalValue > BigInteger.ZERO) {
            val remainder = decimalValue % BigInteger.valueOf(targetBase.toLong())
            targetDigits.insert(0, remainder.toString(targetBase).lowercase())
            decimalValue /= BigInteger.valueOf(targetBase.toLong())
        }
        return targetDigits.toString()
    }