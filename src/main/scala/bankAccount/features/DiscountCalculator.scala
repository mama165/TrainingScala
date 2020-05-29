package bankAccount.features

import bankAccount.models.Types.Amount

object DiscountCalculator {
  def applyDiscount(years: Int): Amount =
    if (years > 10) 1000000L else 0L
}
