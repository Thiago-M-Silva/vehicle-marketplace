import { IBackendErrorMessageInterface } from '@/interfaces/backendErrorMessageInterface';
import axios from 'axios';

export const execRequest = async (
    method: string, 
    url: string, 
    data: any
): Promise<any | IBackendErrorMessageInterface> => {
    try {
        const res = axios.request(
            {
                method: method,
                url: url,
                data: data
            }
        );

        return res;
    } catch (error: any) {
        console.log(error);
        throw error as IBackendErrorMessageInterface;
    }
}