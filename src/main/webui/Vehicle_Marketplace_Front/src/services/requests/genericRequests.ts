import axios from 'axios';

export const execRequest = async (method: string, url: string, data: any) => {
    try {
        const res = axios.request(
            {
                method: method,
                url: url,
                data: data
            }
        );

        return res;
    } catch (error) {
        console.log(error);
        return error;
    }
}