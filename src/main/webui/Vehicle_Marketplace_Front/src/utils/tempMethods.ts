export const dataToShowInResume = (kind: string): any[] => {
        const values: any[] = []
        
        switch (kind) {
            case 'bikes':
                values.push(
                    {
                        name: 'bike1',
                        description: 'See some of our most popular motorbikes.'
                    },
                    {
                        name: 'bike2',
                        description: 'See some of our most popular boats.'
                    },
                    {
                        name: 'bike3',
                        description: 'See some of our most popular cars.'
                    },
                    {
                        name: 'bike4',
                        description: 'See some of our most popular planes.'
                    },
                    {
                        name: 'bike5',
                        description: 'See some of our most popular planes.'
                    },
                    {
                        name: 'bike6',
                        description: 'See some of our most popular planes.'
                    }
                )
                break;
                case 'boats':
                values.push(
                    {
                        name: 'boat1',
                        description: 'See some of our most popular motorboats.'
                    },
                    {
                        name: 'boat2',
                        description: 'See some of our most popular boats.'
                    },
                    {
                        name: 'boat3',
                        description: 'See some of our most popular cars.'
                    },
                    {
                        name: 'boat4',
                        description: 'See some of our most popular planes.'
                    },
                    {
                        name: 'boat5',
                        description: 'See some of our most popular planes.'
                    },
                    {
                        name: 'boat6',
                        description: 'See some of our most popular planes.'
                    }
                )
                break;
                case 'cars':
                values.push(
                    {
                        name: 'cars1',
                        description: 'See some of our most popular cars.'
                    },
                    {
                        name: 'cars2',
                        description: 'See some of our most popular boats.'
                    },
                    {
                        name: 'cars3',
                        description: 'See some of our most popular cars.'
                    },
                    {
                        name: 'cars4',
                        description: 'See some of our most popular planes.'
                    },
                    {
                        name: 'cars5',
                        description: 'See some of our most popular planes.'
                    },
                    {
                        name: 'cars6',
                        description: 'See some of our most popular planes.'
                    }
                )
                break;
                case 'boats':
                values.push(
                    {
                        name: 'boat1',
                        description: 'See some of our most popular motorbikes.'
                    },
                    {
                        name: 'boat2',
                        description: 'See some of our most popular boats.'
                    },
                    {
                        name: 'boat3',
                        description: 'See some of our most popular cars.'
                    },
                    {
                        name: 'boat4',
                        description: 'See some of our most popular planes.'
                    },
                    {
                        name: 'boat5',
                        description: 'See some of our most popular planes.'
                    },
                    {
                        name: 'boat6',
                        description: 'See some of our most popular planes.'
                    }
                )
                break;
            default:
                break;
        }

        return values;
    }