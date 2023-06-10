import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Button, Card, CardContent, TextField, Select, MenuItem, IconButton, Container, Box, styled, Grid } from '@material-ui/core';
import { AddCircleOutline, RemoveCircleOutline } from '@material-ui/icons';
import { BASE_URL } from './config';
import { Autocomplete } from '@material-ui/lab';

// 定义数据类型
type DrugLifeCycleVO = {
    drugName: string;
    drugId: string;
    physicalMarking: string;
    targetConsumer: string;
    operationVOList: {
        operationType: string;
        operationMsg: string;
        operatorAdd: string;
    }[];
};

// 定义操作类型
const operationTypes = ["Normal_Step", "Attack_Availability", "Attack_Confidentiality", "Attack_Integrity"];
const operatorAddList = ["Normal_Step", "Attack_Availability", "Attack_Confidentiality", "Attack_Integrity"];

const StyledCard = styled(Card)(({ theme }) => ({
    marginTop: theme.spacing(2),
    padding: theme.spacing(2),
    border: '1px solid',
    borderColor: theme.palette.grey[300],
    backgroundColor: theme.palette.common.white,
    color: theme.palette.grey[900],
}));



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

    // 获取初始数据
    useEffect(() => {
        axios.get(`${BASE_URL}/web/cards`).then((response) => {
            setData(response.data);
            console.log(response)
        });
    }, []);

    // 获取operationType列表并构建operationTypeMap
    useEffect(() => {
        axios.get(`${BASE_URL}/web/operationTypes`).then((response) => {
            const newOperationTypeMap = new Map();
            response.data.forEach((fullType: string) => {
                const displayType = fullType.substring(fullType.lastIndexOf('.') + 1);
                newOperationTypeMap.set(displayType, fullType);
            });
            setOperationTypeMap(newOperationTypeMap);
        });
    }, []);

    // 提交数据
    const handleSubmit = () => {
        axios.post(`${BASE_URL}/web/submit`, data);
    };

    // 在列表中添加操作
    const addOperation = (index: number, operationIndex: number) => {
        const newData = [...data];
        // 将新条目插入到当前条目之后
        newData[index].operationVOList.splice(operationIndex + 1, 0, {
            operationType: operationTypes[0],
            operationMsg: 'Default MSG',
            operatorAdd: 'Blank',
        });
        setData(newData);
    };


    // 在列表中删除操作
    const deleteOperation = (index: number, operationIndex: number) => {
        const newData = [...data];
        newData[index].operationVOList.splice(operationIndex, 1);
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
                        <Box component="h3" sx={{ color: 'grey.700', mb: 5}}>Expected Buyer: {item.targetConsumer}</Box>

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
                            <Grid container spacing={2} key={operationIndex}>
                                <Grid item xs={3}>
                                    <StyledSelect
                                        value={operationTypeMap.get(operation.operationType)}
                                        onChange={(e) => {
                                            const newData = [...data];
                                            // 当选中值改变时，保存fullType而不是displayType
                                            newData[index].operationVOList[operationIndex].operationType = operationTypeMap.get(e.target.value as string) || '';
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
                                    <IconButton onClick={() => addOperation(index, operationIndex)}>
                                        <AddCircleOutline />
                                    </IconButton>
                                    <IconButton onClick={() => deleteOperation(index, operationIndex)}>
                                        <RemoveCircleOutline />
                                    </IconButton>
                                </Grid>
                            </Grid>
                        ))}
                    </CardContent>
                </StyledCard>
            ))}
            <Button onClick={handleSubmit} variant="contained" style={{ backgroundColor: '#0070c9', color: 'white', marginTop: '16px' }}>Submit</Button>

        </Container>
    );
}
