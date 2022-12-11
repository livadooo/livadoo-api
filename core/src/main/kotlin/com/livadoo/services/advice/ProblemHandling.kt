package com.livadoo.services.advice

import com.livadoo.services.advice.general.GeneralAdviceTrait
import com.livadoo.services.advice.network.NetworkAdviceTrait
import com.livadoo.services.advice.validation.ValidationAdviceTrait

interface ProblemHandling : GeneralAdviceTrait, NetworkAdviceTrait, ValidationAdviceTrait