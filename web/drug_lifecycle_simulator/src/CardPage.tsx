import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Button, Card, CardContent, TextField, Select, MenuItem, IconButton, Container, Box, styled, Grid } from '@material-ui/core';
import { AddCircleOutline, RemoveCircleOutline } from '@material-ui/icons';
import { BASE_URL_V1, BASE_URL_WS } from './config';
import { Autocomplete } from '@material-ui/lab';
import { v4 as uuidv4 } from 'uuid';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';


// Define data type
type DrugLifeCycleVO = {
    batchId: string;
    drugName: string;
    drugId: string;
    physicalMarking: string;
    targetConsumer: string;
    expectedDose: number;
    operationVOList: {
        id: string;
        operationType: string;
        operationMsg: string;
        operatorAdd: string;
    }[];
};

// Define data type
const operationTypes = ["Normal_Step", "Attack_Availability", "Attack_Confidentiality", "Attack_Integrity"];

const StyledCard = styled(Card)(({ theme }) => ({
    marginTop: theme.spacing(2),
    padding: theme.spacing(2),
    border: '1px solid',
    borderColor: theme.palette.grey[300],
    backgroundColor: theme.palette.common.white,
    color: theme.palette.grey[900],
}));


// Style definitions
const StyledSelect = styled(Select)(({ theme }) => ({
    backgroundColor: theme.palette.common.white,
    color: theme.palette.grey[900],
}));

const StyledTextField = styled(TextField)(({ theme }) => ({
    '& label.Mui-focused': {
        color: theme.palette.grey[500],
    },
    '& .MuiInput-underline:after': {
        borderBottomColor: theme.palette.grey[500],
    },
    '& .MuiOutlinedInput-root': {
        '& fieldset': {
            borderColor: theme.palette.grey[500],
        },
        '&:hover fieldset': {
            borderColor: theme.palette.grey[500],
        },
        '&.Mui-focused fieldset': {
            borderColor: theme.palette.grey[500],
        },
    },
    '& .MuiInputBase-input': {
        color: theme.palette.grey[900],
    }
}));


export function CardPage() {
    const [data, setData] = useState<DrugLifeCycleVO[]>([]);
    const [operationTypeMap, setOperationTypeMap] = useState<Map<string, string>>(new Map());
    const [webSocket, setWebSocket] = useState<Client | null>(null);
    const [progress, setProgress] = useState<string[]>([]);
    const [operatorAddList, setOperatorAddList] = useState<string[]>([]);

    // Fetch initial data and operationTypeMap
    useEffect(() => {
        Promise.all([
            axios.get(`${BASE_URL_V1}/web/cards`),
            axios.get(`${BASE_URL_V1}/web/operationTypes`)
        ]).then(([cardsResponse, operationTypesResponse]) => {
            const newOperationTypeMap = new Map();
            operationTypesResponse.data.forEach((fullType: string) => {
                const displayType = fullType.substring(fullType.lastIndexOf('.') + 1);
                newOperationTypeMap.set(displayType, fullType);
            });
            setOperationTypeMap(newOperationTypeMap);

            const processedData = cardsResponse.data.map((item: DrugLifeCycleVO) => {
                return {
                    ...item,
                    operationVOList: item.operationVOList.map((operation) => {
                        const displayType = Array.from(newOperationTypeMap.keys()).find(key => newOperationTypeMap.get(key) === operation.operationType);
                        return {
                            ...operation,
                            operationType: displayType || operation.operationType
                        }
                    })
                }
            });

            setData(processedData);

            // Use the batchId of the first item to get the operator address list
            if (processedData.length > 0) {
                axios.get(`${BASE_URL_V1}/web/operationAdds`, {
                    params: {
                        batchId: processedData[0].batchId
                    }
                }).then(operatorAddResponse => {
                    setOperatorAddList(operatorAddResponse.data);
                });
            }
        });
    }, []);


    // Disconnect from WebSocket when the component unmounts
    useEffect(() => {
        return () => {
            if (webSocket !== null) {
                webSocket.deactivate();
            }
        };
    }, [webSocket]);


    // submit
    const handleSubmit = () => {
        const processedData = data.map((item: DrugLifeCycleVO) => {
            return {
                ...item,
                operationVOList: item.operationVOList.map((operation) => {
                    const fullType = operationTypeMap.get(operation.operationType) || '';
                    return {
                        ...operation,
                        operationType: fullType
                    }
                })
            }
        });

        // axios.post(`${BASE_URL}/web/submit`, processedData);

        const wsUuid = uuidv4();
        const sock = new SockJS(`${BASE_URL_WS}/process/${wsUuid}`);
        const stompClient = new Client({ webSocketFactory: () => sock });

        stompClient.onConnect = (frame) => {
            console.log("subscribe: " + `/topic/process-progress/${wsUuid}`);
            stompClient.subscribe(`/topic/process-progress/${wsUuid}`, (message) => {
                if (message.body) {
                    console.log(`${message.body}`);
                    setProgress(prevProgress => [...prevProgress, `${message.body}`]);
                }
            });
        };

        stompClient.onStompError = (frame) => {
            console.log(`Broker reported error: ${frame.headers.message}`);
            console.log(`Additional details: ${frame.body}`);
        };
        setWebSocket(stompClient);
        stompClient.activate();


        axios.post(`${BASE_URL_V1}/web/submit/${wsUuid}`, processedData);

    };



    // add operation from the list
    const addOperation = (index: number, operationId: string) => {
        const newData = [...data];
        const newOperation = {
            id: uuidv4(),  // 使用uuid生成新operation的唯一标识符
            operationType: Array.from(operationTypeMap.values())[0] || '',
            operationMsg: 'Default MSG',
            operatorAdd: 'Blank',
        };
        const operationIndex = newData[index].operationVOList.findIndex(operation => operation.id === operationId);
        newData[index].operationVOList.splice(operationIndex + 1, 0, newOperation);
        setData(newData);
    };


    // Delete operation from the list
    const deleteOperation = (index: number, operationId: string) => {
        const newData = [...data];
        newData[index].operationVOList = newData[index].operationVOList.filter(operation => operation.id !== operationId);
        setData(newData);
    };

    return (
        <Container>
            {data.map((item, index) => (
                <StyledCard key={index}>
                    <CardContent>
                        <Box component="h2" sx={{ fontSize: '1.5em', color: 'grey.900' }}>{item.drugName}</Box>
                        <Box component="h3" sx={{ color: 'grey.700' }}>Drug Id: {item.drugId}</Box>
                        <Box component="h3" sx={{ color: 'grey.700' }}>Drug Physical Marking: {item.physicalMarking}</Box>
                        <Box component="h3" sx={{ color: 'grey.700'}}>Expected Buyer: {item.targetConsumer}</Box>
                        <Box component="h3" sx={{ color: 'grey.700', mb: 5}}> Expected Dose: {item.expectedDose}</Box>
                        <Grid container spacing={2}>
                            <Grid item xs={3}>
                                <Box component="h4" sx={{ color: 'grey.800' }}>Operation Type</Box>
                            </Grid>
                            <Grid item xs={3}>
                                <Box component="h4" sx={{ color: 'grey.800' }}>Operation Message</Box>
                            </Grid>
                            <Grid item xs={3}>
                                <Box component="h4" sx={{ color: 'grey.800' }}>Operator Address</Box>
                            </Grid>
                            <Grid item xs={3}>
                                <Box component="h4" sx={{ color: 'grey.800' }}>Actions</Box>
                            </Grid>
                        </Grid>

                        {item.operationVOList.map((operation, operationIndex) => (
                            <Grid container spacing={2} key={operation.id}>
                                <Grid item xs={3}>
                                    <StyledSelect
                                        value={operation.operationType}
                                        onChange={(e) => {
                                            const newData = [...data];
                                            newData[index].operationVOList[operationIndex].operationType = e.target.value as string;
                                            setData(newData);
                                        }}
                                        fullWidth
                                    >
                                        {Array.from(operationTypeMap.keys()).map((displayType) => (
                                            <MenuItem key={displayType} value={displayType}>
                                                {displayType}
                                            </MenuItem>
                                        ))}
                                    </StyledSelect>
                                </Grid>
                                <Grid item xs={3}>
                                    <StyledTextField
                                        value={operation.operationMsg}
                                        onChange={(e) => {
                                            const newData = [...data];
                                            newData[index].operationVOList[operationIndex].operationMsg = e.target.value;
                                            setData(newData);
                                        }}
                                        fullWidth
                                    />
                                </Grid>
                                <Grid item xs={3}>
                                    <Autocomplete
                                        options={operatorAddList} // 这是一个字符串数组，表示所有可能的操作者
                                        getOptionLabel={(option) => option}
                                        value={operation.operatorAdd}
                                        onChange={(event: any, newValue: string | null) => {
                                            const newData = [...data];
                                            newData[index].operationVOList[operationIndex].operatorAdd = newValue || '';
                                            setData(newData);
                                        }}
                                        renderInput={(params) => <StyledTextField {...params} />}
                                    />
                                </Grid>
                                <Grid item xs={3}>
                                    <IconButton onClick={() => addOperation(index, operation.id)}>
                                        <AddCircleOutline />
                                    </IconButton>
                                    <IconButton onClick={() => deleteOperation(index, operation.id)}>
                                        <RemoveCircleOutline />
                                    </IconButton>
                                </Grid>
                            </Grid>
                        ))}
                    </CardContent>
                </StyledCard>
            ))}
            <Button onClick={handleSubmit} variant="contained" style={{ backgroundColor: '#0070c9', color: 'white', marginTop: '16px' }}>Submit</Button>
            {progress.map((prog, index) => (
                <TextField disabled value={prog} fullWidth key={index} />
            ))}
        </Container>
    );
}
