import {configureStore} from "@reduxjs/toolkit" 
import logsReducer from "../features/logsSlice" 

const store = configureStore({
    reducer : {
        logs : logsReducer
    }
}) ;
export default store ; 
