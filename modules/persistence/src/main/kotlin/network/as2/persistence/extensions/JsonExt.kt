package network.as2.persistence.extensions

import org.jooq.JSONB
import org.jooq.tools.json.JSONObject

fun JSONObject.toJSONB(): JSONB = JSONB.valueOf(this.toString())
