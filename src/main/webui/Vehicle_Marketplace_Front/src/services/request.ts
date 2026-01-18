import axios from 'axios';

const execRequest = async (method: string, url: string, data: any) => {
    try {
        const res = axios.request(
            {
                method: method,
                url: url,
                data: data
            }
        );
    } catch (error) {
        
    }
}

export const getAllVehicleByKind = async (params:type) => {}