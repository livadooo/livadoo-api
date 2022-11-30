package com.livadoo.services.customer.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException

class CustomerNotFoundException(
    customerId: String
) : ObjectNotFoundException("Customer not found", "Customer with id $customerId not found")