package es.elb4t.apirestfullrp3

import android.content.ContentValues.TAG
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import org.restlet.data.MediaType
import org.restlet.ext.json.JsonRepresentation
import org.restlet.representation.Representation
import org.restlet.representation.StringRepresentation
import org.restlet.resource.Get
import org.restlet.resource.Post
import org.restlet.resource.ServerResource


class LEDResource : ServerResource() {

    @Get("json")
    fun getState(): Representation {
        val result = JSONObject()
        try {
            result.put("estado", LEDModel.getState())
        } catch (e: Exception) {
            Log.e(TAG, "Error en JSONObject: ", e)
        }

        return StringRepresentation(result.toString(), MediaType.APPLICATION_ALL_JSON)
    }

    @Post("json")
    fun postState(entity: Representation): Representation {
        var query = JSONObject()
        val fullresult = JSONObject()
        var result: String
        try {
            val json = JsonRepresentation(entity)
            query = json.jsonObject
            val state = query.get("estado") as Boolean
            Log.d(this.javaClass.simpleName, "Nuevo estado del LED: $state")
            if (LEDModel.setState(state)) result = "ok" else result = "error"
        } catch (e: Exception) {
            Log.e(TAG, "Error: ", e)
            result = "error"
        }

        try {
            fullresult.put("resultado", result)
        } catch (e: JSONException) {
            Log.e(TAG, "Error en JSONObject: ", e)
        }

        return StringRepresentation(fullresult.toString(), MediaType.APPLICATION_ALL_JSON)
    }
}